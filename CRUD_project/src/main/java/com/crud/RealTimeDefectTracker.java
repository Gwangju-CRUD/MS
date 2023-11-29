package com.crud;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RealTimeDefectTracker extends DefectTracker {

    private RestTemplate restTemplate = new RestTemplate();

    // 실시간으로 양불 판정을 받아오는 메서드
    public void getAndCheckRealTimePrediction() {
        String url = "http://218.157.38.54:8002/apply_weights/";  // 실시간 양불 판정을 받아오는 URL입니다.
        HttpMethod method = HttpMethod.POST;  // HTTP 요청 방식입니다.

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, null, String.class);
        String prediction = responseEntity.getBody();

        // 받아온 판정 결과를 checkDefect 메서드에 넘깁니다.
        checkDefect(prediction);
    }
}
