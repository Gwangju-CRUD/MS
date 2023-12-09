package com.crud.dto;

import java.util.List;

import lombok.Data;

@Data
public class AllCountAnalysis {
  
  // 하루에 해당하는 전체데이터의 정상 불량
  private Long dailyNomalCount;
  private Long dailyErrorCount;

  // 주간에 해당하는 전체데이터의 정상 불량
  private List<Long> weekNomalCount;
  private List<Long> weekErrorCount;

  // 이번달에 해당하는 전체데이터의 정상 불량
  private List<Long> monthNomalCount;
  private List<Long> monthErrorCount;

  // 오늘 날짜를 기준으로 요일을 역순으로 담아오는 리스트
  private List<String> weekList;

}
