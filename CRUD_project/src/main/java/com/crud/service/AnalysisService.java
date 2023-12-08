package com.crud.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crud.DefectTracker;
import com.crud.dto.AllCountAnalysis;
import com.crud.dto.RealTimeAnalysis;
import com.crud.dto.SingleAnalysis;
import com.crud.entity.AlarmLog;
import com.crud.entity.Analysis;
import com.crud.repository.AlarmLogRepository;
import com.crud.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisService {

	@Autowired
	private AnalysisRepository analysisRepository;
	@Autowired
	private AlarmLogRepository alarmLogRepository;

	// DefectTracker 객체를 생성합니다.
	private DefectTracker defectTracker = new DefectTracker();
	

	public void imgSave(Analysis analysis) {
		analysisRepository.save(analysis);

		boolean check = defectTracker.checkDefect(analysis.getPredictionJdm());

		// 슬렉에 알람이 보내지면 로그를 DB에 저장을 하는 로직
		if (check == true) {
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
			String formatDate = now.format(format);

			AlarmLog alarmLog = new AlarmLog();
			alarmLog.setMsg("제품 불량 5개 확인요망");
			alarmLog.setAlarmDate(formatDate);
			alarmLogRepository.save(alarmLog);
		}
		/*
		 * Description : 이미지 분석 결과를 데이터베이스에 저장하고, 분석 결과가 불량인지 아닌지를 체크합니다. Params :
		 * analysis - 이미지 분석 결과가 담긴 Analysis 객체 Returns : x
		 */
	}

	// Order By해서 리스트 값 리턴하기(이미지)
	public Page<Analysis> getAllAnalysis(int page) {

		/**
		 * Description : 제품 분석 결과를 저장한 데이터베이스에서 특정 페이지의 분석 결과 목록을 가져옵니다. 가져온 분석 결과 목록은
		 * 제품 인덱스(productIdx)를 기준으로 내림차순으로 정렬되며, 한 페이지당 10개의 분석 결과를 포함합니다. Params : page
		 * - 가져올 분석 결과 목록의 페이지 번호 Returns : 해당 페이지의 분석 결과 목록 (Page<Analysis>)
		 */
		Pageable pageable = PageRequest.of(page, 10); // 한 페이지당 10개만 보여주겠다는 의미
		Page<Analysis> PageList = analysisRepository.findAllByOrderByProductIdxDesc(pageable);
		return PageList;
	}

	// 단건 분석 결과를 count해주는 로직 
	// main 페이지에서 사용
	public Long singleAnalysisCount(){
		Long singleAnalysisCount = analysisRepository.countByPredictionClassfication("단건");
		return singleAnalysisCount;
	}

	public Long realTimeAnalysisCount(){
		Long realTimeAnalysisCount = analysisRepository.countByPredictionClassfication("실시간");
		return realTimeAnalysisCount;
	}

	// 실시간 분석 로그의 정상과 불량 카운트를 객체에 담아 컨트롤러에 전달
	public Map<String, Object> AnalysisCount(){
		String normal = "정상";
		String error = "불량";
		String realTime = "실시간";
		String single = "단건";

		RealTimeAnalysis realTimeAnalysis = new RealTimeAnalysis();
		SingleAnalysis singleAnalysis = new SingleAnalysis();

		// 실시간 정상 
		realTimeAnalysis.setNormalRealTimeAnalysis(analysisRepository.countByPredictionClassficationAndPredictionJdm(realTime, normal));

		// 실시간 불량
		realTimeAnalysis.setErrorRealTimeAnalysis(analysisRepository.countByPredictionClassficationAndPredictionJdm(realTime, error));

		// 단건 정상
		singleAnalysis.setNormalSingleAnalysis(analysisRepository.countByPredictionClassficationAndPredictionJdm(single, normal));
		
		// 단건 불량
		singleAnalysis.setErrorSingleAnalysis(analysisRepository.countByPredictionClassficationAndPredictionJdm(single, error));

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("real",realTimeAnalysis);
		resultMap.put("single",singleAnalysis);

		return resultMap;
	}

	// 일, 주간, 월에 대한 count 로직
  public AllCountAnalysis allCountAnalysis() {

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일");

		// 객체 생성
		AllCountAnalysis allCountAnalysis = new AllCountAnalysis();

		// 해당 일당 정상 불량 set 코드
		Long dailyNomalCount;
		Long dailyErrorCount;

		dailyNomalCount = analysisRepository.countByPredictionDateAndPredictionJdmLike(now.format(format), "정상");
		dailyErrorCount = analysisRepository.countByPredictionDateAndPredictionJdmLike(now.format(format), "불량");

		allCountAnalysis.setDailyNomalCount(dailyNomalCount);
		allCountAnalysis.setDailyErrorCount(dailyErrorCount);

		// 주간당 정상 불량 set 코드
		List<Long> weekCountNormalList = new ArrayList<>();
		List<Long> weekCountErrorList = new ArrayList<>();

		for(int i=0; i<7; i++){

			weekCountNormalList.add(analysisRepository.countByPredictionDateAndPredictionJdmLike(now.minusDays(i).format(format), "정상"));
			weekCountErrorList.add(analysisRepository.countByPredictionDateAndPredictionJdmLike(now.minusDays(i).format(format), "불량"));
		}
		allCountAnalysis.setWeekNomalCount(weekCountNormalList);
		allCountAnalysis.setWeekErrorCount(weekCountErrorList);

		// 해당 월당 정상 불량 set 코드
		List<Long> monthCountNomalList = new ArrayList<>();
		List<Long> monthCountErrorList = new ArrayList<>();

		for(int i=1; i<13; i++){
			monthCountNomalList.add(analysisRepository.countByPredictionDateAndPredictionJdmLike(String.format("2023년 %d월", i), "정상"));
			monthCountErrorList.add(analysisRepository.countByPredictionDateAndPredictionJdmLike(String.format("2023년 %d월", i), "불량"));
		}
		allCountAnalysis.setMonthNomalCount(monthCountNomalList);
		allCountAnalysis.setMonthErrorCount(monthCountErrorList);

    return allCountAnalysis;
  }


}
