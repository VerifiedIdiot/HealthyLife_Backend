package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;


    private final RestTemplate restTemplate;

    @Value("${ninja.api.url}")
    private String apiUrl;

    @Value("${ninja.api.key}")
    private String apiKey;

    @Value("${papago.api.client-id}")
    private String papagoClientId;

    @Value("${papago.api.client-secret}")
    private String papagoClientSecret;
    @Autowired
    private ExerciseRepository repository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.restTemplate = new RestTemplate();
    }

    private Exercise mapToExerciseEntity(ExerciseDto exerciseDto) {
        Exercise exercise = exerciseDto.toExerciseEntity();
        return exercise;
    }

    private ExerciseDto entityToExerciseDto(Exercise exercise) {
        return exercise.toExerciseDto();}

    public void insertExercises() {
        int offset = 0;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        boolean moreData = true;

        while (moreData) {
            ResponseEntity<ExerciseDto[]> responseEntity = restTemplate.exchange(apiUrl + "?offset=" + offset, HttpMethod.GET, entity, ExerciseDto[].class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ExerciseDto[] response = responseEntity.getBody();

                if (response == null || response.length == 0) {
                    moreData = false;
                } else {
                    // 응답 배열을 순회하면서 번역하고 저장합니다.
                    for (ExerciseDto originalExerciseDto : response) {
                        // 번역 서비스를 호출하여 ExerciseDto를 번역합니다.
                        ExerciseDto translatedExerciseDto = this.translateExerciseDto(originalExerciseDto);

                        // 변환된 Exercise 엔티티를 리포지토리를 통해 데이터베이스에 저장합니다.
                        Exercise exercise = mapToExerciseEntity(translatedExerciseDto);
                        exerciseRepository.save(exercise);

                        // 저장된 운동 정보를 콘솔에 출력합니다.
                        System.out.println("Exercise: " + exercise);
                    }
                }
                offset += response.length;
            } else {
                System.out.println("Error: " + responseEntity.getStatusCode());
            }
        }
    }
    private ExerciseDto translateExerciseDto(ExerciseDto originalExerciseDto) {
        // 각 필드를 번역하고 새로운 ExerciseDto를 생성
        String translatedName = translateText(originalExerciseDto.getName());
        String translatedType = translateText(originalExerciseDto.getType());
        String translatedMuscle = translateText(originalExerciseDto.getMuscle());
        String translatedDifficulty = translateText(originalExerciseDto.getDifficulty());

        ExerciseDto translatedExerciseDto = new ExerciseDto();
        translatedExerciseDto.setName(translatedName);
        translatedExerciseDto.setType(translatedType);
        translatedExerciseDto.setMuscle(translatedMuscle);
        translatedExerciseDto.setDifficulty(translatedDifficulty);

        return translatedExerciseDto;
    }

    private String translateText(String originalText) {
        String apiUrl = "https://openapi.naver.com/v1/papago/n2mt";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.set("X-Naver-Client-Id", papagoClientId);
        headers.set("X-Naver-Client-Secret", papagoClientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("source", "en"); // 전체 코드에서는 영어로 가정합니다.
        body.add("target", "ko"); // 전체 코드에서는 한국어로 번역합니다.
        body.add("text", originalText);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Papago API 응답에서 번역된 텍스트 추출
            return extractTranslatedText(responseEntity.getBody());
        } else {
            // 오류 처리
            return "번역 실패";
        }
    }

    private String extractTranslatedText(String responseBody) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("message").path("result").path("translatedText").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return "번역 실패";
        }
    }
}
