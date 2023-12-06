package com.crud.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.RequestMember;

public interface RequestMemberRepository extends JpaRepository<RequestMember, String>{
		
	Page<RequestMember> findAll(Pageable pageable);
	
}
