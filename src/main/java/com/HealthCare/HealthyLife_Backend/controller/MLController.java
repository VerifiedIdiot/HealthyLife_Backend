package com.HealthCare.HealthyLife_Backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/ml")
@RequiredArgsConstructor
public class MLController {

    // Flask API로 POST 요청을 보내고 예측 결과를 받아오는 엔드포인트
    @PostMapping("/life")
    public ResponseEntity<String> consumePredictions(@RequestBody String inputJson) {
        log.info("요청합니다!!!!!!" + inputJson);

        // Flask API의 URL
        String flaskApiUrl = "http://localhost:5000/predict";

        // 요청에 사용할 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // JSON 데이터를 Flask API로 POST 요청을 보내고 결과를 받아옴
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(flaskApiUrl, new HttpEntity<>(inputJson, headers), String.class);

        // 응답을 처리하거나 필요에 따라 반환
        return responseEntity;
    }
}
