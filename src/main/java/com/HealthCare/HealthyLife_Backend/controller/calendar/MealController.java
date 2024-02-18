package com.HealthCare.HealthyLife_Backend.controller.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.service.calendar.MealService;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/meal")
public class MealController {
    private final MealService mealService;

    // 식단 추가
    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestBody(required = true) MealDto mealDto) {
        try {
            System.out.println(mealDto);
            mealService.addAndUpdateCalendar(mealDto);
            return ResponseEntity.ok("음식추가");
        } catch (Exception e) {
            log.error("음식 추가 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long id) {
        try {
            boolean deleted = mealService.deleteMeal(id);
            if (deleted) {
                return ResponseEntity.ok().body("Meal deleted successfully.");
            } else {
                // 삭제 로직에서 false 반환 시, 예외는 발생하지 않았으나 삭제가 실패한 경우
                return ResponseEntity.badRequest().body("Failed to delete meal.");
            }
        } catch (EntityNotFoundException e) {
            // 식사 기록을 찾을 수 없는 경우
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 그 외 예외 처리
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    // 음식 검색
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


    @GetMapping("/findAll")
    public ResponseEntity<?> findAllData() {
        try {
            List<MealDto> results = mealService.findAll();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 조회 실패: " + e.getMessage());
        }
    }

    // 출력
    @JsonView(Views.Internal.class)
    @GetMapping("/detail")
    public ResponseEntity<List<MealDto>> MealByCalendarId(@RequestParam Long calendarId) {
        List<MealDto> list = mealService.getMealByCalendarId(calendarId);
        System.out.println(list);
        return ResponseEntity.ok(list);
    }


    // 식단 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<String> mealModify(
            @PathVariable Long id,
            @RequestBody MealDto mealDto) {
        try{
            ResponseEntity<String> modifyMeal = mealService.modifyMeal(id, mealDto);
            return modifyMeal;
        } catch (Exception e) {
            log.error("음식 수정 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
