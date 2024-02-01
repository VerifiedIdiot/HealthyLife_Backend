package com.HealthCare.HealthyLife_Backend.controller.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.service.calendar.MealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/meal")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String type) {
        try {
            MealDto results = mealService.addMealWithFood(keyword, id, type);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error");
            return ResponseEntity.badRequest().body("검색 실패: " + e.getMessage());
        }
    }
}
