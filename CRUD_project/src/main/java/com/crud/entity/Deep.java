package com.crud.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_deep")
public class Deep {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deep_seq")
	@SequenceGenerator(name = "deep_seq", sequenceName = "DEEP_SEQ")
	private Long deep_idx; // 번호

	@Lob
	private byte[] product_img; // 분석할 이미지

	private Long prediction_accuracy; // 정확도

	private String prediction_jdm; // 판정결과

	private LocalDateTime created_at; // 판정시간

	@ManyToOne
	@JoinColumn(name = "mb_id")
	private Member member; // 회원 엔터티 참조

}
