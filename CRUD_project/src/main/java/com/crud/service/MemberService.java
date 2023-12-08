package com.crud.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.DuplicateIdException;
import com.crud.entity.Member;
import com.crud.entity.RequestMember;
import com.crud.repository.MemberRepository;
import com.crud.repository.RequestMemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RequestMemberRepository requestMemberRepository;
	// 2. 회원가입 요청 메소드 만들기

	// 유저 회원요청 로직
	public boolean request(String mbId) {
		if (memberRepository.findBymbId(mbId).isPresent()) {
			return false;
		} else {
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

	// 전체 회원 조회
	public Page<Member> getAllMember(int page) {
		Pageable pageable = PageRequest.of(page, 10); // 한 페이지당 10개만 보여주겠다는 의미
		Page<Member> PageList = memberRepository.findAll(pageable);
		return PageList;
	}

	// 로그인 회원 조회
	public Member getUserByMbId(String mbId) {
	    Optional<Member> optionalMember = memberRepository.findBymbId(mbId);
	    if (optionalMember.isPresent()) {
	        return optionalMember.get();
	    } else {
	        throw new RuntimeException("회원을 찾을 수 없습니다."); // 예외 처리 방식은 필요에 따라 수정 가능합니다.
	    }
	}

	// 이미지
	   public Member updateProfileImage(String mbId, String imagePath) {
	  Optional<Member> optionalMember = memberRepository.findBymbId(mbId);
	  if(optionalMember.isPresent()) { Member member = optionalMember.get();
	  member.setProfileImg(imagePath); memberRepository.save(member); return
	  member; }else { return null; }
	  
	  }
	 

	// 회원 요청 로직
	public void requestMember(RequestMember requestMember) {

		this.requestMemberRepository.save(requestMember);

	}

	public Page<RequestMember> getRequestMember(int page) {
		Pageable pageable = PageRequest.of(page, 10); // 한 페이지당 10개만 보여주겠다는 의미
		Page<RequestMember> guestPageList = requestMemberRepository.findAll(pageable);
		return guestPageList;
	}

	
	

	//요청 회원 삭제
	public void deleteMember(String mbId) {
		requestMemberRepository.deleteById(mbId);
	}
		
		
		
	
	}

	

