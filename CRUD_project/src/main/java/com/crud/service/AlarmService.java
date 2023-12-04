package com.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.crud.repository.AlarmLogRepository;
import com.crud.entity.AlarmLog;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {

  @Autowired
	private AlarmLogRepository alarmLogRepository;

  public List<AlarmLog> alarmList(){
    List<AlarmLog> alarmLogList = alarmLogRepository.findTop5ByOrderByAlarmIdxDesc();
    return alarmLogList;
  }

}
