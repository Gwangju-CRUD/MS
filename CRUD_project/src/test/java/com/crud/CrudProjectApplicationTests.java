package com.crud;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.crud.repository.MemberRepository;

@SpringBootTest
class CrudProjectApplicationTests {

	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void 멤버_테스트() {
		
	}

}
