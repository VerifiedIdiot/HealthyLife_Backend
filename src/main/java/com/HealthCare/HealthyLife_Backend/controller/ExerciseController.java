package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.repository.ExerciseRepository;
import com.HealthCare.HealthyLife_Backend.service.ExerciseService;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Controller
//@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/api/exercise")
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;
    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/insert")
    public ResponseEntity<String> uploadFoodData() {
        exerciseService.insertExercises();
        return ResponseEntity.ok("Exercise data uploaded and processed successfully!");
    }

    @GetMapping("/list/insert")
    public ResponseEntity<List<ExerciseDto>> exerciseList(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        List<ExerciseDto> list = exerciseService.getExerciseList(page, size);
        log.info("list : {}", list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/view/search")
    public ResponseEntity<List<ExerciseDto>> getExerciseSearchView(@RequestParam(required = false) String keyword,
                                                                   @RequestParam(required = false) String muscle,
                                                                   @RequestParam(required = false) String difficulty,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "27") int size) {
        try {
            List<ExerciseDto> exercisePage = exerciseService.getExerciseSortedByKeywordAndMuscleAndDifficulty(keyword, muscle, difficulty, page, size);
            return ResponseEntity.ok(exercisePage);
        } catch (Exception e) {
            log.error("운동 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}