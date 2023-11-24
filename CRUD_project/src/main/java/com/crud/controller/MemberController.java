package com.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crud.form.MemberForm;
import com.crud.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 속성의 생성자를 자동으로 생성해줌 
@Controller
@RequestMapping("/member")

public class MemberController {
	
	private final MemberService memberService;
	
	
	@GetMapping("/signup")
	public String signup(MemberForm memberForm) {
		return "member/signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "member/signup_form";
		}
		if (!memberForm.getMbPw1().equals(memberForm.getMbPw2())) {
			bindingResult.rejectValue("mb_pw2", "passwordIncorrect", "비밀번호를 확인해주세요");
			return "member/signup_form";
		}
		try {
			this.memberService.create(
					memberForm.getMbId(), 
					memberForm.getMbPw1(), 
					memberForm.getMbName(), 
					memberForm.getMbCompany());
		} catch (Exception e) {
			bindingResult.reject("signupfail", "이미 등록된 유저입니다.");
			return "member/signup_form";
		}
		return "redirect:/main";
	}
	
	
	
	@GetMapping("/login")
	public String login() {
		return "member/login_form";
	}
	
}