package com.crud;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefectTracker {
	private int defectCount = 0;
	// 클래스 내부에서만 접근 가능한 정수형 변수 defectCount를 선언하고, 이 변수의 초기값을 0으로 설정
	public void checkDefect(String predictionJdm) {
		/*
		* Description : 제품의 품질 예측 결과를 받아, 이 결과가 불량인지 아닌지를 확인하고,
		               불량일 경우 불량 제품 수를 증가시킵니다. 불량 제품 수가 5개 이상일 경우
 		               Slack으로 알림을 보낸 후, 불량 제품 수를 초기화합니다.
 		 Params      : predictionJdm - 제품의 품질 예측 결과
		 Retruns     : X
		*/
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

	private void sendSlackNotification() {
		/*
		* Description : 불량 판정이 5번 이상 반복될 경우, 해당 정보를 Slack에 알림으로 전송하는 메서드입니다.
		*              알림은 Slack 웹훅을 통해 POST 요청으로 전송되며, 메시지 전송이 성공하면 콘솔에 성공 메시지를 출력하고,
		               실패하면 실패 메시지를 출력합니다.
		 Params      : x
		 Returns     : x
		*/
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// Slack에 보낼 메시지를 설정합니다.
		String payload = "{'text':'진영준 오케이.'}";

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
