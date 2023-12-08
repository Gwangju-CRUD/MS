package com.crud.repository;


import java.time.LocalDateTime;

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

	// 전체 정상과 불량에 관한 모든 count 데이터를 DB에서 가져오는 JPA작성
	Long countByPredictionDateAndPredictionJdmLike(String jdm, String predictionDate);
}
