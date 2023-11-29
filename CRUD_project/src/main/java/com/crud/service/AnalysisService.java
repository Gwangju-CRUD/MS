package com.crud.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crud.DefectTracker;
import com.crud.entity.Analysis;
import com.crud.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisService {

	@Autowired
	private AnalysisRepository analysisRepository;
	
	 // DefectTracker 객체를 생성합니다.
    private DefectTracker defectTracker = new DefectTracker();
	
	public void imgSave(Analysis analysis) {
		analysisRepository.save(analysis);
		
		defectTracker.checkDefect(analysis.getPredictionJdm());
		/*
		  * Description : 이미지 분석 결과를 데이터베이스에 저장하고, 
		  *               분석 결과가 불량인지 아닌지를 체크합니다.
		  * Params      : analysis - 이미지 분석 결과가 담긴 Analysis 객체
		  * Returns     : x
		*/
	}

	// Order By해서 리스트 값 리턴하기(이미지)
	public Page<Analysis> getAllAnalysis(int page) {

		/**
		 * Description : 제품 분석 결과를 저장한 데이터베이스에서 특정 페이지의 분석 결과 목록을 가져옵니다.
		 *               가져온 분석 결과 목록은 제품 인덱스(productIdx)를 기준으로 내림차순으로 정렬되며,
		 *               한 페이지당 10개의 분석 결과를 포함합니다.
		 * Params      : page - 가져올 분석 결과 목록의 페이지 번호
		 * Returns     : 해당 페이지의 분석 결과 목록 (Page<Analysis>)
		 */
		
		Pageable pageable = PageRequest.of(page, 10); // 한 페이지당 10개만 보여주겠다는 의미
		Page<Analysis> PageList = analysisRepository.findAllByOrderByProductIdxDesc(pageable);
		return PageList;
	}

}
