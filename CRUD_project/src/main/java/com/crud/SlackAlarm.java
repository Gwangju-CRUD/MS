package com.crud;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SlackAlarm  {
	   private int defectCount = 0;

	   // 불량 카운트 로직
	   public void checkDefect(String predictionJdm) {
	      if ("불량".equals(predictionJdm)) {
	         defectCount++;

	         if (defectCount >= 5) {
	            sendSlackNotification();
	            defectCount = 0; // 알림을 보낸 후에는 카운터를 초기화합니다.
	         }
	      } else {
	         defectCount = 0; // 양호한 제품이 확인되면 카운터를 초기화합니다.
	      }
	   }

	   // 불량 5개 이상일 시 알람보내주는 로직
	   private void sendSlackNotification() {
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.set("Content-Type", "application/json");

	      // Slack에 보낼 메시지를 설정합니다.
	      String payload = "{'text':'불량판정이 5번 이상 발생했습니다.'}";

	      HttpEntity<String> request = new HttpEntity<>(payload, headers);

	      // Slack 웹훅 URL에 POST 요청을 보냅니다.
	      ResponseEntity<String> response = restTemplate.exchange(
	            "https://hooks.slack.com/services/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", // 본인의 Slack 웹훅
	                                                                              // URL로 변경해주세요.
	            HttpMethod.POST, request, String.class);

	      if (response.getStatusCode().is2xxSuccessful()) {
	         System.out.println("Slack 메시지 전송 성공");
	      } else {
	         System.out.println("Slack 메시지 전송 실패");
	      }
	   }
	}