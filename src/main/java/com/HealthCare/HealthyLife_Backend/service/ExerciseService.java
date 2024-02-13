package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {

    @Autowired
    private final ExerciseRepository exerciseRepository;


    private final RestTemplate restTemplate;

    @Value("${ninja.api.url}")
    private String apiUrl;

    @Value("${ninja.api.key}")
    private String apiKey;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.restTemplate = new RestTemplate();
    }

    private Exercise mapToExerciseEntity(ExerciseDto exerciseDto) {
        Exercise exercise = exerciseDto.toExerciseEntity();
        return exercise;
    }

    private ExerciseDto entityToExerciseDto(Exercise exercise) {
        return exercise.toExerciseDto();
    }

    public void insertExercises() {
        int offset = 0;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        boolean moreData = true;

        exerciseRepository.deleteAll();

        while (moreData) {
            ResponseEntity<ExerciseDto[]> responseEntity = restTemplate.exchange(apiUrl + "?offset=" + offset, HttpMethod.GET, entity, ExerciseDto[].class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ExerciseDto[] response = responseEntity.getBody();

                if (response == null || response.length == 0) {
                    moreData = false;
                } else {
                    for (ExerciseDto originalExerciseDto : response) {
                        Exercise exercise = mapToExerciseEntity(originalExerciseDto);
                        exerciseRepository.save(exercise);

                        System.out.println("Exercise: " + exercise);
                    }
                }
                offset += response.length;
            } else {
                System.out.println("Error: " + responseEntity.getStatusCode());
            }
        }
    }

    public List<ExerciseDto> getExerciseList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Exercise> exercisePage = exerciseRepository.findAll(pageable);

        List<Exercise> exercises = exercisePage.getContent();
        List<ExerciseDto> exerciseDtos = new ArrayList<>();
        for (Exercise exercise : exercises) {
            ExerciseDto exerciseDto = exercise.toExerciseDto(); // Food 엔티티를 FoodDto로 변환
            exerciseDtos.add(exerciseDto);
        }
        return exerciseDtos;
    }

    public List<ExerciseDto> getExerciseSortedByKeywordAndMuscleAndDifficulty(String keyword, String muscle, String difficulty, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Exercise> exercises;

        if (keyword != null || muscle != null || difficulty != null) {
            exercises = exerciseRepository.findByConditions(keyword, muscle, difficulty, pageable);
        } else {
            // 모든 조건이 null인 경우 모든 음식 데이터를 반환
            exercises = exerciseRepository.findAll(pageable);
        }

        List<ExerciseDto> exerciseDtos = new ArrayList<>();
        for (Exercise exercise : exercises.getContent()) {
            exerciseDtos.add(exercise.toExerciseDto());
        }
        return exerciseDtos;
    }
}