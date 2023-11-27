package com.crud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.crud.DuplicateIdException;
import com.crud.form.MemberForm;
import com.crud.service.MemberService;

import groovyjarjarpicocli.CommandLine.DuplicateNameException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 속성의 생성자를 자동으로 생성해줌
@Controller

public class MemberController {

	private final MemberService memberService;

	// 관리자만 보이도록 추후 설정할 것
	@GetMapping("/signup")
	public String signup(MemberForm memberForm) {
		return "member/signup_form";
	}

	// 관리자 회원가입 시큐리티
	@PostMapping("/signup")
	public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "member/signup_form";
		}
		if (!memberForm.getMbPw1().equals(memberForm.getMbPw2())) {
			bindingResult.rejectValue("mbPw2", "passwordIncorrect", "비밀번호를 확인해주세요");
			return "member/signup_form";
		}
		try {
			this.memberService.create(memberForm.getMbId(), memberForm.getMbPw1(), memberForm.getMbName(),
					memberForm.getMbCompany());
		} catch (DuplicateIdException e) {
			bindingResult.reject("signupfail", "이미 등록된 아이디입니다.");
			return "member/signup_form";
		} 

		return "redirect:/main";
	}

	// 가장 처음 시작하는 로그인 페이지
	@GetMapping("/")
	public String login() {
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