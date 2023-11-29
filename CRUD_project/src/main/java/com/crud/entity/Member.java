package com.crud.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_member")
public class Member {

	@Id
	private String mb_id; // 아이디

	@Column(nullable = false)
	private String mb_pw; // 비밀번호

	@Column(nullable = false)
	private String mb_name; // 이름

	@Column(nullable = false)
	private String mb_company; // 회사명

	private LocalDateTime joined_at; // 회원가입한 날짜

	private LocalDateTime last_at; // 마지막 로그인 날짜

	@Column(length = 1000, columnDefinition = "varchar2(1000) default 'local'")
	private String profile_img; // 프로필 이미지

	
}
