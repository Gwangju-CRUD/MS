package com.crud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경설정 파일임을 명시 - 스프링 시큐리티 설정을 위해 사용
@EnableWebSecurity // 스프링 시큐리티를 활성화 - 스프링 시큐리티 설정을 할 수 있게 해줌
public class SecurityConfig {
	/* 스프링 시큐리티란 ?
	 * - 사용자의 인증과 권한을 담당하는 스프링 프레임워크
	 * - 인증: 로그인, 권한: 역할
	 * - 시큐리티가 자체적으로 가지고 있는 인증기능 또는 외부의 인증 시스템(소셜 로그인)등을 지원함
	 * - 인증된 사용자가 어디까지 접근할 수 있는지 권한을 설정
	 * - 세션을 공격으로부터 방어하고 관리함
	 * - 암호화와 해싱을 제공함
	 * - 작동원리 : 필터체인을 통해 HTTP 요청을 처리
	 *  		   - 일련의 필터들로 이루어진 것
	 *  		   - 필터들은 각각 저마다 특정 보안 관련 작업을 수행
	 * */
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http // HTTP 요청에 대한 보안 설정을 시작
			// 모든 경로에 대한 HTTP 요청은 인증 없이 접근 가능하도록 설정
			.authorizeHttpRequests((authorizeHttpRequests)-> authorizeHttpRequests					
					.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
			// CSRF - 웹 사이트 취약점 공격을 방지하기 위한 기술.
			// CSRF 검증을 비활성화 하기 위한 문장 - 개발단계에서는 테스트를 쉽게 할 수 있도록
			// 실제 운영 환경에서는 CSRF 검증을 활성화 해줘야함.
			.csrf((csrf)-> csrf
					.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
			// X-Frame-Option 헤더를 설정
			// 브라우저가 페이지를 렌더링할 수 있도록 해줌
			// 주로 클릭재킹 공격을 방지하기 위해 사용
			.headers((headers)->headers
					.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
			.formLogin((formLogin)-> { formLogin
					.loginPage("/login") // 로그인시 어디로 이동해야할지 알려줌
					.failureUrl("/?error")
					.defaultSuccessUrl("/main")
					.usernameParameter("mbId")
					.passwordParameter("mbPw");
					
			})
			
			.logout((logout)->logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 요청시 이동하는 페이지
					.logoutSuccessUrl("/") // 로그아웃 성공시 이동
					.invalidateHttpSession(true)) // 로그아웃시 세션 삭제			
			;
		return http.build();	
	}
	
	// 암호화하는 빈
	@Bean
	PasswordEncoder passwordEncoder() {
		// 비밀번호를 암호화하기 위해 BCrypt 해싱 함수를 사용하는 PasswordEncoder 빈을 생성함
		return new BCryptPasswordEncoder();
	}
	
	// 인증과정을 관리하는 빈
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
		// AuthenticationManager - 얘가 인증을 관리함
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
