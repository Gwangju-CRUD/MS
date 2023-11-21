package com.crud.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "tbl_community")
public class Community {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_seq")
	@SequenceGenerator(name = "community_seq", sequenceName = "COMMUNITY_SEQ")
	private Long b_idx; // 글번호

	@Column(nullable = false)
	private String b_title; // 글 제목

	@Column(nullable = false)
	@Lob
	private String b_content; // 글 내용

	private String b_file; // 글 첨부파일

	private LocalDateTime created_at; // 글 작성일자

	@Column(columnDefinition = "number(18,0) default 0")
	private Long b_views; // 글 조회수

	@Column(columnDefinition = "number(18,0) default 0")
	private Long b_likes; // 글 좋아요 수

	@ManyToOne
	@JoinColumn(name = "mb_id")
	private Member member; // 회원 엔터티 참조

	@OneToMany(mappedBy = "community")
	private Set<Comment> comments = new HashSet<>(); // 댓글 목록

}
