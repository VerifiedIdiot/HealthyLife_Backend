package com.HealthCare.HealthyLife_Backend.controller.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.service.calendar.MealService;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/meal")
public class MealController {
    private final MealService mealService;
//    private final MemeberSevice memeberSevice;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> add(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String id,
//            @RequestParam(required = false) String type) {
//        try {
//            MealDto results = mealService.addMealWithFood(keyword, id, type);
//            return ResponseEntity.ok(results);
//        } catch (Exception e) {
//            log.error("Error");
//            return ResponseEntity.badRequest().body("검색 실패: " + e.getMessage());
//        }
//    }
    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestBody(required = true) MealDto mealDto) {
        try {
            mealService.addMealWithFood(mealDto);
            return ResponseEntity.ok("z");
        } catch (Exception e) {
            log.error("음식 추가 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @JsonView(Views.Internal.class)
    @GetMapping("/view/search")
    public ResponseEntity<List<FoodDto>> getFoodByKeyword(
            @RequestParam(required = true) String keyword) {
        try {
            List<FoodDto> foodDtos = mealService.getFoodKeyword(keyword);
            return ResponseEntity.ok(foodDtos);
        } catch (Exception e) {
            log.error("음식 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


}
