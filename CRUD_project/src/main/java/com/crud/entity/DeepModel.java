package com.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_DEEP_MODEL")
public class DeepModel {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODEL_SEQ")
	@SequenceGenerator(name = "MODEL_SEQ", sequenceName = "MODEL_SEQ")
	private Long modelIdx; // 제품 번호
	
	@Column(name = "MODEL")
	@Lob
	private byte[] model;
	
	@Column(name = "FILE_NAME")
	private String fileName; 
	
	@ManyToOne
	@JoinColumn(name = "mbId")
	private Member member;
}
