package com.crud.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
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
		
		private String last_at; // 마지막 로그인 날짜
		
		@Column(length = 1000, columnDefinition = "varchar2(1000) default 'local'")
		private String profile_img; // 프로필 이미지
		

		@Builder
		public Member(String mb_id, String mb_pw, String mb_name, String mb_company, LocalDateTime joined_at,
				String last_at, String profile_img) {
			this.mb_id = mb_id;
			this.mb_pw = mb_pw;
			this.mb_name = mb_name;
			this.mb_company = mb_company;
			this.joined_at = joined_at;
			this.last_at = last_at;
			this.profile_img = profile_img;
		}
		
		
}
