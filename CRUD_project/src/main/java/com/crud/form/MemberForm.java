package com.crud.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberForm {


	@NotEmpty(message = "아이디를 입력해주세요")
	private String mbId;
	
	@NotEmpty(message = "비밀번호를 입력해주세요")
	private String mbPw1;
	
	@NotEmpty(message = "비밀번호를 확인해주세요")
	private String mbPw2;
	
	@NotEmpty(message = "이름을 입력해주세요")
	private String mbName;
	
	@NotEmpty(message = "회사명을 입력해주세요")
	private String mbCompany;
	
	
}
