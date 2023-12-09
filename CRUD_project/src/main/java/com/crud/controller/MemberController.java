package com.crud.controller;

import groovy.lang.GString;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crud.DuplicateIdException;
import com.crud.entity.Member;
import com.crud.entity.RequestMember;
import com.crud.form.MemberForm;
import com.crud.repository.MemberRepository;
import com.crud.repository.RequestMemberRepository;
import com.crud.service.MemberService;
import groovyjarjarpicocli.CommandLine.DuplicateNameException;
import jakarta.transaction.Transactional;
import com.crud.entity.AlarmLog;
import com.crud.form.MemberForm;
import com.crud.service.AlarmService;
import com.crud.service.AnalysisService;
import com.crud.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 속성의 생성자를 자동으로 생성해줌
@Controller

public class MemberController {

	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final RequestMemberRepository requestMemberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AnalysisService analysisService;

	private final AlarmService alarmService;

	// 관리자만 보이도록 추후 설정할 것
	@GetMapping("/signup")
	public String signup(MemberForm memberForm) {
		return "member/signup_form";
	}

	@PostMapping("/memberManagement")
	public String signup(@Valid MemberForm memberForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("msg", "모든 입력창을 입력해주세요.");
			return "member/memberManagement";
		}
		if (!memberForm.getMbPw1().equals(memberForm.getMbPw2())) {
			model.addAttribute("msg", "비밀번호를 확인해주세요.");
			return "member/memberManagement";
		}
		try {
			this.memberService.create(memberForm.getMbId(), memberForm.getMbPw1(), memberForm.getMbName(),
					memberForm.getMbCompany());
		} catch (DuplicateIdException e) {
			model.addAttribute("msg", "이미 등록된 아이디입니다.");
			return "member/memberManagement";
		}
		return "member/memberManagement";
	}

	// 유저 요청 회원가입 시큐리티
	@PostMapping("/")
	public String signupRequest(@Valid MemberForm memberForm, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("msg", "예기치 못한 에러가 발생했습니다.");
			return "member/login_form";
		}

		// 비밀번호 일치 확인
		if (!memberForm.getMbPw1().equals(memberForm.getMbPw2())) {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "member/login_form";
		}

		if (memberService.request(memberForm.getMbId()) == true) {
			model.addAttribute("msg", "요청이 완료되었습니다");

			// 1. memberService에 요청 메소드 만들기
			RequestMember requestMember = new RequestMember();

			LocalDateTime now = LocalDateTime.now();

			requestMember.setMbId(memberForm.getMbId());
			requestMember.setMbPw(memberForm.getMbPw1());
			requestMember.setMbName(memberForm.getMbName());
			requestMember.setMbCompany(memberForm.getMbCompany());
			requestMember.setJoineDate(now);

			memberService.requestMember(requestMember);

			// 3. DB에 회원의 값 저장하기

			return "member/login_form";
		} else {
			model.addAttribute("msg", "동일한 아이디가 있습니다");
			return "member/login_form";
		}

	}

	// ------- 관리자 페이지에서 db로 저장을 하는 컨트롤러 및 서비스 로직 구성

	// 가장 처음 시작하는 로그인 페이지
	@GetMapping("/")
	public String login(Model model) {
		model.addAttribute("memberForm", new MemberForm());
		return "member/login_form";
	}

	// 로그인이 아무 이상없이 성공하면 main으로 이동함
	@GetMapping("/main")
	public String goMain(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			
			
			
			
			String username = authentication.getName();
			System.out.println("인증된 사용자: " + username);

			// 모델로 객체 받아와서 네비바에 Member프로필 이미지,이름 불러오기
			Optional<Member> optionalMember = memberRepository.findBymbId(username);
			if (optionalMember.isPresent()) {
				Member member = optionalMember.get();
				model.addAttribute("member", member);
			}

			List<AlarmLog> alarmList = alarmService.alarmList();
			Long memberCount = memberService.memberCount();
			Long singleAnalysisCount = analysisService.singleAnalysisCount();
			Long realTimeAnalysisCount = analysisService.realTimeAnalysisCount();

			model.addAttribute("memberCount", memberCount);
			model.addAttribute("alarmList", alarmList);
			model.addAttribute("singleAnalysisCount", singleAnalysisCount);
			model.addAttribute("realTimeAnalysisCount", realTimeAnalysisCount);
			
			return "main";

		} else {
			System.out.println("사용자가 인증되지 않았습니다");
			return "member/login_form";
		}

	}

	// ------- 네비바 컨트롤러 -------

	@GetMapping("/memberManagement")
	public String memberManagement(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
		// 전체 회원 조회
		Page<Member> memberList = this.memberService.getAllMember(page);
		model.addAttribute("memberForm", new MemberForm());
		model.addAttribute("memberList", memberList);

		// 요청 회원 조회
		Page<RequestMember> requestMemberList = this.memberService.getRequestMember(page);
		model.addAttribute("requestMemberList", requestMemberList);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Optional<Member> optionalMember = memberRepository.findBymbId(username);
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			model.addAttribute("member", member);
		}

		return "member/memberManagement";
	}

	// 로그인 회원 조회 > 프로필
	@GetMapping("/myPage")
	public String showMyPage(Model model, Authentication authentication) {
		// 현재 로그인한 사용자의 회원 정보를 가져옵니다.
		String mbId = authentication.getName();
		Optional<Member> optionalMember = memberRepository.findBymbId(mbId);
		if (optionalMember.isPresent()) {
			// 모델에 회원 정보를 담아 myPage.html로 전달합니다.
			model.addAttribute("member", optionalMember.get());
		} else {
			// ID에 해당하는 회원이 없을 때의 처리도 필요합니다.
		}

		return "myPage";
	}

	// 이미지 업로드
	/**
	 * Description : 사용자가 선택한 이미지 파일을 서버에 업로드하고, 이미지 파일의 경로를 사용자 정보에 저장하는 메소드입니다.
	 * 
	 * Params      : 
	 * @param file - 사용자가 업로드한 이미지 파일
	 * @param authentication - 현재 로그인한 사용자의 인증 정보
	 * @param redirectAttributes - 리다이렉트 시 속성을 추가하기 위한 객체
	 * 
	 * Returns     : 
	 * @return String - 성공 시 'redirect:/myPage'를 반환하여 마이페이지로 리다이렉트, 실패 시 에러 메시지를 반환합니다.
	 */
	
	@PostMapping("/myPage")
	public String upload(@RequestParam("file") MultipartFile file, Authentication authentication, RedirectAttributes redirectAttributes) {
		String fileName = file.getOriginalFilename();
		String uploadDir = System.getProperty("user.dir") + "/CRUD_project/src/main/resources/static/imgFolder/";
		File dest = new File(uploadDir + fileName);
		try {
			
			file.transferTo(dest);
			String imagePath = "/imgFolder/" + fileName;

			// 현재 로그인한 사용자의 회원 정보를 가져옵니다.
			String mbId = authentication.getName();
			Optional<Member> optionalMember = memberRepository.findBymbId(mbId);

			if (optionalMember.isPresent()) {
				Member member = optionalMember.get();
				member.setProfileImg(imagePath);
				memberRepository.save(member);
			}

			return "redirect:/myPage";
		}

		catch (Exception e) {
			e.printStackTrace();
			return "파일 업로드에 실패했습니다.";
		}
		
	}

	// 회원 추가 및 요청회원 삭제
	@PostMapping("/members/approve/{mbId}")
	public String approveMember(@PathVariable String mbId) {
		Optional<RequestMember> requestMember = requestMemberRepository.findById(mbId);

		if (requestMember.isPresent()) {
			Member member = new Member();

			// RequestMember의 정보를 Member로 복사
			member.setMbId(requestMember.get().getMbId());
			member.setMbPw(passwordEncoder.encode(requestMember.get().getMbPw()));
			member.setMbCompany(requestMember.get().getMbCompany());
			member.setMbName(requestMember.get().getMbName());
			member.setJoineDate(requestMember.get().getJoineDate());

			// Member 저장
			memberRepository.save(member);

			// RequestMember 삭제
			requestMemberRepository.deleteById(mbId);
		}
		return "redirect:/memberManagement"; // 처리 후 리다이렉트할 경로
	}

	// 요청 회원 삭제
	@PostMapping("/members/delete/{mbId}")
	public String deleteMember(@PathVariable String mbId) {
		requestMemberRepository.deleteById(mbId);
		return "redirect:/memberManagement"; // 삭제 후 리다이렉트할 경로
	}

	@GetMapping("allResult")
	public String allResult(Model model) {
		// 모델로 객체 받아와서 네비바에 Member프로필 이미지,이름 불러오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Optional<Member> optionalMember = memberRepository.findBymbId(username);
		if (optionalMember.isPresent()) {
        Member member = optionalMember.get();
        model.addAttribute("member", member);
    }


		return "analysis/allResult";
	}



	@GetMapping("/singleAnalysis")
	public String singleAnalysis(Model model){
		// 모델로 객체 받아와서 네비바에 Member프로필 이미지,이름 불러오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Optional<Member> optionalMember = memberRepository.findBymbId(username);
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			model.addAttribute("member", member);
		}

		return "analysis/singleAnalysis";
	}

}