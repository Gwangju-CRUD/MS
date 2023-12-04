package com.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "TBL_ORI_IMAGE")
public class OriginImage {

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORI_IMAGE_SEQ")
	@SequenceGenerator(name = "ORI_IMAGE_SEQ", sequenceName = "ORI_IMAGE_SEQ", allocationSize = 1)
	private Long oriImageIdx; // 제품 번호
	
	
	@Lob
	@Column(nullable = false, name = "ORI_IMAGE")
	private byte[] oriImage; // 제품 원본 이미지
	
	
}
