package com.crud.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.Deep;

public interface DeepRepository extends JpaRepository<Deep, Long>{
	
}
