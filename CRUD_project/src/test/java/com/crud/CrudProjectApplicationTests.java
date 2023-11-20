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
	public void testMember() throws Exception {
		
	}

}
