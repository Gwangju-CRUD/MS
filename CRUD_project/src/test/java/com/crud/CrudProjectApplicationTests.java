package com.crud;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.crud.entity.AlarmLog;
import com.crud.repository.AlarmLogRepository;


@SpringBootTest
class CrudProjectApplicationTests {

	@Autowired
	private AlarmLogRepository alarmLogRepository;
	
	@Test
	public void db데이터_테스트() {
		
		for(int i=1; i<3; i++) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
			String formatDate = now.format(format);
			
			AlarmLog alarmLog = new AlarmLog();
			alarmLog.setMsg("제품 불량 5개 확인요망");
			alarmLog.setAlarmDate(formatDate);

			alarmLogRepository.save(alarmLog);
		}
		
	}

}
