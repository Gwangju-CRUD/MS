package com.crud.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
	
	Optional<Member> findBymbId(String mbId);

	Page<Member>findAll(Pageable pageable);

	Long countBy();

}

