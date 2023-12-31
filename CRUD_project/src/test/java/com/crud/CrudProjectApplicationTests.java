package com.crud;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.crud.entity.Analysis;
import com.crud.entity.Member;
import com.crud.repository.AnalysisRepository;
import com.crud.repository.MemberRepository;
import com.crud.entity.AlarmLog;
import com.crud.entity.Member;
import com.crud.repository.AlarmLogRepository;
import com.crud.repository.MemberRepository;


@SpringBootTest
class CrudProjectApplicationTests {

	@Autowired
	private AlarmLogRepository alarmLogRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void db데이터_테스트() {
		
		for(int i=1; i<3; i++) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
			String formatDate = now.format(format);
			
			AlarmLog alarmLog = new AlarmLog();
			alarmLog.setMsg("제품 불량 5개가 감지되었습니다.");
			alarmLog.setAlarmDate(formatDate);

			alarmLogRepository.save(alarmLog);
		}
		
	}

	@Test
	public void 멤버더미데이터(){

		LocalDateTime now = LocalDateTime.now();

		for(int i=51; i<100; i++){
			Member member = new Member();
			member.setMbId(String.format("가즈아%d", i));
			member.setMbName("가즈아");
			member.setJoineDate(now);
			member.setMbCompany("가즈아");
			member.setMbPw("몰라");

			memberRepository.save(member);
		}

		
	}
}
