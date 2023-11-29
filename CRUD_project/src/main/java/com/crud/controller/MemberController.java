package com.crud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.crud.DuplicateIdException;
import com.crud.form.MemberForm;
import com.crud.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 속성의 생성자를 자동으로 생성해줌
@Controller

public class MemberController {

	private final MemberService memberService;

	// 관리자만 보이도록 추후 설정할 것
	@GetMapping("/signup")
	public String signup(@Valid MemberForm memberForm) {
		return "member/signup_form";
	}

	// 관리자 회원가입 시큐리티
	@PostMapping("/")
	public String signup(@Valid MemberForm memberForm, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "redirect:/";
		}
		
		// 비밀번호 일치 확인
		if (!memberForm.getMbPw1().equals(memberForm.getMbPw2())) {
			bindingResult.rejectValue("mbPw2", "passwordIncorrect", "비밀번호를 확인해주세요");
			model.addAttribute("pwMsg", "비밀번호가 일치하지 않습니다.");
			return "redirect:/";
		}
		
		// 멤버를 DB에 저장하는 로직
		try {
			// 아이디가 있는 없는 지를 판단하는 SERVICE만들기 O
			// SERVICE 호출하기 O
			memberService.request(memberForm.getMbId());
			
			
			bindingResult.reject("signupsucess", "요청이 완료되었습니다.");
			
			// 중복된 아이디가 없으면 관리자에게 정보를 넘겨주기 X
			return "redirect:/";
			
			// 관리자 페이지로 이동하게 하면 됨. X
		} catch (Exception e) {
			
			bindingResult.reject("signupfail", "이미 등록된 아이디입니다.");
			return "redirect:/";
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
	public String goMain() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			System.out.println("인증된 사용자: " + username);
			return "main";
		} else {
			System.out.println("사용자가 인증되지 않았습니다");
			return "member/login_form";
		}

	}
	
	

}