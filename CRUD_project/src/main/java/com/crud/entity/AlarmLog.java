package com.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_ALARM_LOG")
public class AlarmLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALARM_LOG_SEQ")
	@SequenceGenerator(name = "ALARM_LOG_SEQ", sequenceName = "ALARM_LOG_SEQ", allocationSize = 1)
	private Long alarmIdx; // 알람 번호

	@Column(nullable = false)
	private String msg; // 알람 메세지

	@Column(nullable = false)
	private String alarmDate; // 알람시간

}
