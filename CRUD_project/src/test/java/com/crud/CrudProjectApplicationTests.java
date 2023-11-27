package com.crud;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.crud.entity.Analysis;
import com.crud.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;

@SpringBootTest
class CrudProjectApplicationTests {

	@Autowired
	private AnalysisRepository analysisRepository;
	
	@Test
	public void db데이터_테스트() {
		
		for(int i=1; i<100; i++) {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
	        String formatDate = now.format(format);
			
			Analysis analysis = new Analysis();
			analysis.setPredictionAccuracy(80L);
			analysis.setProductImg(null);
			analysis.setPredictionJdm("정상");
			analysis.setPredictionDate(formatDate);
			this.analysisRepository.save(analysis);
		}
		
	}

}
