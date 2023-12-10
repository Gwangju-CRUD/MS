package com.crud.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "TBL_REQUEST_MEMBER")
@Entity
@Data
public class RequestMember {

	@Id
	private String mbId; // 아이디

	@Column(nullable = false)
	private String mbPw; // 비밀번호

	@Column(nullable = false)
	private String mbName; // 이름

	@Column(nullable = false)
	private String mbCompany; // 회사명
	@Column(nullable = false)
	private LocalDateTime joineDate; // 회원가입한 날짜
	
	@Column(nullable = false, length = 1000)
	private String profileImg = "imgFolder/profile.png"; // 프로필 이미지
}
