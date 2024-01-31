package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.repository.ExerciseRepository;
import com.HealthCare.HealthyLife_Backend.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController implements CrudControllerInterface<ExerciseDto, Long> {

    private final ExerciseService exerciseService;

    private final ExerciseRepository exerciseRepository;
    public ExerciseController(ExerciseService exerciseService, ExerciseRepository exerciseRepository) {
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    @PostMapping("/insert")
    public ResponseEntity<?> insert() {
        try {
            exerciseService.insertExercises();
            return ResponseEntity.ok("운동정보 테이블 insert");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<ExerciseDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ExerciseDto> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ExerciseDto> update(Long id, ExerciseDto exerciseDto) {
        return null;
    }


}