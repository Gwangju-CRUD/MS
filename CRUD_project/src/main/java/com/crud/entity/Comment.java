package com.crud.entity;

import java.time.LocalDateTime;
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
@Table(name = "tbl_comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
	@SequenceGenerator(name = "comment_seq", sequenceName = "COMMENT_SEQ")
	private Long cmt_idx; // 댓글 번호

	@ManyToOne
	@JoinColumn(name = "b_idx")
	private Community community; // 원글 번호 (Community 엔터티 참조)

	@Column(nullable = false)
	@Lob
	private String cmt_content; // 댓글 내용

	private LocalDateTime created_at; // 댓글 작성일자

	@ManyToOne
	@JoinColumn(name = "mb_id")
	private Member member; // 댓글 작성자 (Member 엔터티 참조)

	@Column(columnDefinition = "number(18,0) default 0")
	private Long cmt_likes; // 댓글 좋아요 수

}