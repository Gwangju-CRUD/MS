package com.crud.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.entity.Member;
import com.crud.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

		private final MemberRepository memberRepository;
		private final PasswordEncoder passwordEncoder;
	

		public Member create(String mbId , String mbPw, String mbName, String mbCompany) {
		
			Member member = new Member();
			member.setMbId(mbId);
			member.setMbPw(passwordEncoder.encode(mbPw));
			member.setMbName(mbName);
			member.setMbCompany(mbCompany);
			member.setJoineDate(LocalDateTime.now());
			member.setProfileImg("몰라");
			this.memberRepository.save(member);
			return member;
}
}