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
import com.crud.entity.Analysis;
import com.crud.entity.Member;
import com.crud.entity.RequestMember;
import com.crud.repository.AnalysisRepository;
import com.crud.repository.MemberRepository;
import com.crud.repository.RequestMemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final AnalysisRepository analysisRepository;
	private final PasswordEncoder passwordEncoder;
	private final RequestMemberRepository requestMemberRepository;
	// 2. 회원가입 요청 메소드 만들기

	/**
	 * Description : 사용자의 회원 가입 요청을 처리하는 메소드입니다. 이미 존재하는 아이디로 가입을 요청하면 false를 반환합니다.
	 * 
	 * Params      : 
	 * @param mbId - 회원 가입을 요청하는 사용자의 아이디
	 * 
	 * Returns     : 
	 * @return boolean - 회원 가입 요청이 성공하면 true를 반환하고, 이미 존재하는 아이디로 가입을 요청하면 false를 반환합니다.
	 */

	// 유저 회원요청 로직
	public boolean request(String mbId) {
		if (memberRepository.findBymbId(mbId).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Description : 새로운 사용자를 생성하는 메소드입니다. 이미 존재하는 아이디로 사용자를 생성하려고 하면 예외를 발생시킵니다.
	 * 
	 * Params      : 
	 * @param mbId - 생성할 사용자의 아이디
	 * @param mbPw - 생성할 사용자의 비밀번호
	 * @param mbName - 생성할 사용자의 이름
	 * @param mbCompany - 생성할 사용자의 회사 이름
	 * 
	 * Returns     : 
	 * @return Member - 생성된 사용자의 정보를 반환합니다. 이미 존재하는 아이디로 사용자를 생성하려고 하면 예외를 발생시키고 사용자 정보를 반환하지 않습니다.
	 */

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
		member.setProfileImg("imgFolder/profile.png");
		this.memberRepository.save(member);

		return member;
	}

	// 모든 회원의 수를 count하는 service
	public Long memberCount(){
		return memberRepository.countBy();
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

	/**
	 * Description : 사용자의 프로필 이미지 경로를 업데이트하는 메소드입니다.
	 * 
	 * Params      : 
	 * @param mbId - 이미지를 업데이트할 사용자의 아이디
	 * @param imagePath - 사용자의 새로운 프로필 이미지 경로
	 * 
	 * Returns     : 
	 * @return Member - 프로필 이미지가 업데이트된 사용자의 정보를 반환합니다. 해당 사용자가 없을 경우 null을 반환합니다.
	 */

	// 이미지 업데이트
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

	// 나의 분석 기록 service
    public Page<Analysis> myAnalysisLog(String mbId, int page) {
		Pageable pageable = PageRequest.of(page, 10);
		Page<Analysis> myAnalysisList = analysisRepository.findByMember_MbIdOrderByProductIdxDesc(mbId, pageable);

		return myAnalysisList;
    }
		
		
		
	
	}

	

