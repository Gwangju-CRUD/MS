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

}
