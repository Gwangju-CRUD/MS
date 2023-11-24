package com.crud.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_MEMBER")
public class Member {

	@Id
	@Column(unique = true)
	private String mbId; // 아이디

	private String mbPw; // 비밀번호
	
	@Column(unique = true)
	private String mbName; // 이름
	
	@Column(unique = true)
	private String mbCompany; // 회사명

	private LocalDateTime joineDate; // 회원가입한 날짜

	@Column(length = 1000, columnDefinition = "varchar2(1000) default 'local'")
	private String profileImg; // 프로필 이미지

	
}
