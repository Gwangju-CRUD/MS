package com.crud.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.DuplicateIdException;
import com.crud.entity.Member;
import com.crud.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	// 유저 회원요청 로직
	public boolean request(String mbId) {
		if (memberRepository.findBymbId(mbId).isPresent()) {
			return false;
		}else {
			return true;
		}
	}


	// 관리자 회원가입 로직
	public Member create(String mbId, String mbPw, String mbName, String mbCompany) {

		if (memberRepository.findBymbId(mbId).isPresent()) {
			throw new DuplicateIdException("이미 등록된 유저입니다.");
		}
		Member member = new Member();
		member.setMbId(mbId);
		member.setMbPw(passwordEncoder.encode(mbPw));
		member.setMbName(mbName);
		member.setMbCompany(mbCompany);
		member.setJoineDate(LocalDateTime.now());
		member.setProfileImg("local");
		this.memberRepository.save(member);

		return member;
	}

	// 모든 회원의 수를 count하는 service
	public Long memberCount(){
		return memberRepository.countBy();
	}
}