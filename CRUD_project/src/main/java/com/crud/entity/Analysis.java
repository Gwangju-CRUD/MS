package com.crud.entity;

import java.util.Base64;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_ANALYSIS")
public class Analysis {

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANALYSIS_SEQ")
	@SequenceGenerator(name = "ANALYSIS_SEQ", sequenceName = "ANALYSIS_SEQ", allocationSize = 1)
	private Long productIdx; // 제품 번호

	@Lob
	@Column(nullable = false, name = "PRODUCT_IMG")
	private byte[] productImg; // 제품 이미지
	
	@Column(name = "PREDICTION_ACCURACY")
	private double predictionAccuracy; // 에측 정확도

	@Column(name = "PREDICTION_JDM")
	private String predictionJdm; // 예측 결과

	@Column(name = "PREDICTION_DATE")
	private String predictionDate; // 예측 시간

	@Column(name = "PREDICTION_CLASSFICATION")
	private String predictionClassfication; // 예측 분류 - 단건 or 실시간

	@ManyToOne
    @JoinColumn(name = "mbId")
	private Member member; // 회원 엔터티 참조
	
	// base64인코딩
	public String getBase64ProductImg() {
        return Base64.getEncoder().encodeToString(this.productImg);
    }

}
