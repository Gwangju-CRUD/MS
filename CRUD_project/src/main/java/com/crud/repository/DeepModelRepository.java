package com.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.DeepModel;

public interface DeepModelRepository extends JpaRepository<DeepModel, Long>{
	List<DeepModel> findByMember_MbId(String mbId);
}
