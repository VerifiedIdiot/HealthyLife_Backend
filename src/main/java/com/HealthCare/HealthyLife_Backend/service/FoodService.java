package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Value("${excel.file.path}")
    private String excelPath;

    @Transactional
    public void saveFoodData() {
        try {
            List<FoodDto> foodDtoList = FoodDto.readFromExcel(excelPath);
            List<Food> foodEntities = new ArrayList<>();

            for (FoodDto foodDto : foodDtoList) {
                foodEntities.add(foodDto.toFoodEntity());
            }

            foodRepository.saveAll(foodEntities);
        } catch (IOException e) {
            // 예외 처리 로직을 여기에 추가
            e.printStackTrace();
        }
    }
}

