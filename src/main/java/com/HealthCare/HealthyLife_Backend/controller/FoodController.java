package com.HealthCare.HealthyLife_Backend.controller;
import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.service.BodyService;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Controller
@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/api/food")
@Slf4j
public class FoodController {

    private final FoodService foodService;
    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFoodData() {
        foodService.saveFoodData();
        return ResponseEntity.ok("Food data uploaded and processed successfully!");
    }

    @GetMapping("/list/page")
    public ResponseEntity<List<FoodDto>> foodList(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        List<FoodDto> list = foodService.getFoodList(page, size);
        log.info("list : {}", list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/view/search")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<FoodDto>> getFoodSearchView(@RequestParam String keyword) {
        try {
            List<FoodDto> foods = foodService.getFoodSortedByKeyword(keyword);
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            // 데이터가 조회되지 않았을때 발생하는 에러를 처리하기 위한 예외처리
            return ResponseEntity.notFound().build();
        }
    }
}

