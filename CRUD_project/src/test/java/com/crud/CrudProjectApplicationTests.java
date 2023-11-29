package com.crud;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.crud.entity.Member;
import com.crud.repository.MemberRepository;

@SpringBootTest
class CrudProjectApplicationTests {

	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void 멤버_테스트() {
		Member member = new Member();
        
        member.setMb_id("01681747");
        member.setMb_pw("11647439");
        member.setMb_name("진영준");
        member.setMb_company("유니클로 연제점");
        member.setJoined_at(LocalDateTime.now());
        member.setLast_at(LocalDateTime.now());
        member.setProfile_img("경로");
        this.memberRepository.save(member);
	}

}
