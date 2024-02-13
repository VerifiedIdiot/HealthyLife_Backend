package com.HealthCare.HealthyLife_Backend.controller.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.service.calendar.MealService;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date;

@Slf4j
@RestController
@RequestMapping("/meal")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

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
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<List<MealDto>> MealByEmail(
            @RequestParam String email,
            @RequestParam String regDate
            ){
        List<MealDto> list = mealService.getMealByEmail(email, regDate);
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
