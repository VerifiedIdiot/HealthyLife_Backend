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
    @GetMapping("/detail")
    public ResponseEntity<List<MealDto>> MealByCalendarId(@RequestParam Long calendarId) {
        List<MealDto> list = mealService.getMealByCalendarId(calendarId);
//        System.out.println("이메일 : " + email + " 날짜 :" + regDate );
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

    // 식단 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable Long id) {
        try {
            boolean isTrue = mealService.deleteMeal(id);
            return ResponseEntity.ok(isTrue);
        } catch (Exception e) {
            log.error("음식 삭제 중 오류 발생 : {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
