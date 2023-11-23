package com.crud.entity;

import java.time.LocalDateTime;
import java.util.Base64;

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
@Table(name = "TBL_ANALYSIS")
public class Analysis {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deep_seq")
	@SequenceGenerator(name = "deep_seq", sequenceName = "DEEP_SEQ")
	private Long productIdx; // 제품 번호

	@Lob
	@Column(nullable = false, name = "PRODUCT_IMG")
	private byte[] productImg; // 제품 이미지
	
	@Column(name = "PREDICTION_ACCURACY")
	private Long predictionAccuracy; // 에측 정확도

	@Column(name = "PREDICTION_JDM")
	private String predictionJdm; // 예측 결과

	@Column(name = "PREDICTION_DATE")
	private LocalDateTime predictionDate; // 예측 시간

	@ManyToOne
    @JoinColumn(name = "mbId")
	private Member member; // 회원 엔터티 참조
	
	public String getBase64ProductImg() {
        return Base64.getEncoder().encodeToString(this.productImg);
    }

}
