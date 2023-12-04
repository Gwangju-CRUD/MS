package com.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.entity.AlarmLog;

@Repository
public interface AlarmLogRepository extends JpaRepository<AlarmLog, Long>{

  List<AlarmLog> findTop5ByOrderByAlarmIdxDesc();

}
