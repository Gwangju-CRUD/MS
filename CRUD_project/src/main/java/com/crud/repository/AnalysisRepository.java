package com.crud.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.crud.entity.Analysis;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long>{
	
	Page<Analysis> findAllByOrderByProductIdxDesc(Pageable pageable); // OrderBy 하는 로직

	Long countByPredictionClassfication(String predictionClassfication);

	// 실시간, 정상
	Long countByPredictionClassficationAndPredictionJdm(String predictionClassfication, String predictionJdm);
	
	Page<Analysis> findByMember_MbIdAndPredictionClassfication(String mbId, String predictionClassfication, Pageable pageable);

	Page<Analysis> findByMember_MbIdAndPredictionClassficationAndPredictionJdm(String mbId, String predictionClassfication, String predictionJdm, Pageable pageable);
}
