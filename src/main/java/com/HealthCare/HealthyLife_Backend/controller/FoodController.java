package com.HealthCare.HealthyLife_Backend.controller;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Controller
@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/api/food")
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
}

