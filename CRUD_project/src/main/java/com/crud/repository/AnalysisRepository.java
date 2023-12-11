package com.crud.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
	Long countByPredictionDateContainingAndPredictionJdm(String predictionDate, String jdm);
	
	@Query("SELECT new map(a.predictionDate as predictionDate, a.predictionJdm as predictionJdm, a.predictionClassfication as predictionClassfication) FROM Analysis a WHERE a.member.mbId = :mbId")
    List<Map<String, Object>> findSelectedColumnsByMbId(@Param("mbId") String mbId);

	@Query("SELECT new map(a.predictionDate as predictionDate, a.predictionJdm as predictionJdm, a.predictionClassfication as predictionClassfication) FROM Analysis a")
	List<Map<String, Object>> findAllSelectedColumns();

	@Query("SELECT a FROM Analysis a WHERE a.member.mbId = :mbId")
	List<Map<String, Object>> findAllByMbId(@Param("mbId") String mbId);

	Page<Analysis> findByMember_MbIdOrderByProductIdxDesc(String mbId, Pageable pageable);

}
