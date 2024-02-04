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

        if (keyword != null) {
            if (muscle != null) {
                if (difficulty != null) {
                    // keyword, muscle, difficulty 모두 입력된 경우
                    exercises = exerciseRepository.findByNameAndMuscleAndDifficultyContaining(keyword, muscle, difficulty, pageable);
                } else {
                    // keyword와 muscle만 입력된 경우
                    exercises = exerciseRepository.findByNameAndMuscleContaining(keyword, muscle, pageable);
                }
            } else if (difficulty != null) {
                // keyword와 difficulty만 입력된 경우
                exercises = exerciseRepository.findByNameAndDifficultyContaining(keyword, difficulty, pageable);
            } else {
                // keyword만 입력된 경우
                exercises = exerciseRepository.findByNameContaining(keyword, pageable);
            }
        } else if (muscle != null) {
            if (difficulty != null) {
                // muscle과 difficulty만 입력된 경우
                exercises = exerciseRepository.findByMuscleAndDifficultyContaining(muscle, difficulty, pageable);
            } else {
                // muscle만 입력된 경우
                exercises = exerciseRepository.findByMuscleContaining(muscle, pageable);
            }
        } else if (difficulty != null) {
            // difficulty만 입력된 경우
            exercises = exerciseRepository.findByDifficultyContaining(difficulty, pageable);
        } else {
            // 모든 요소가 입력되지 않은 경우 모든 것을 가져옴
            exercises = exerciseRepository.findAll(pageable);
        }

        List<ExerciseDto> exerciseDtos = new ArrayList<>();
        for (Exercise exercise : exercises.getContent()) {
            exerciseDtos.add(exercise.toExerciseDto());
        }
        return exerciseDtos;
    }
}