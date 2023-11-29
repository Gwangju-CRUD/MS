package com.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.crud.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 속성의 생성자를 자동으로 생성해줌 
@Controller
public class MemberController {
	
	private final MemberRepository memberRepository;
	
	@GetMapping("login")
	public String loginForm() {
		return "member/login";
	}
	
	@GetMapping("join")
	public String joinForm() {
		return "member/join";
	}
	
	
}