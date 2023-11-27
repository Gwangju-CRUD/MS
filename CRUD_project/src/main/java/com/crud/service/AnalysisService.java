package com.crud.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crud.entity.Analysis;
import com.crud.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisService {

	@Autowired
	private AnalysisRepository analysisRepository;

	public void imgSave(Analysis analysis) {
		analysisRepository.save(analysis);
	}

	// Order By해서 리스트 값 리턴하기(이미지)
	public Page<Analysis> getAllAnalysis(int page) {

		Pageable pageable = PageRequest.of(page, 10); // 한 페이지당 10개만 보여주겠다는 의미
		Page<Analysis> PageList = analysisRepository.findAllByOrderByProductIdxDesc(pageable);
		return PageList;
	}

}
