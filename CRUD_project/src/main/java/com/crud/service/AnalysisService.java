package com.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<Analysis> getAllAnalysis() {
		return analysisRepository.findAll();
	}

	
}
