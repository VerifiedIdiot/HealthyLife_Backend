package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void saveFoodData(MultipartFile file) {
        try {
            List<FoodDto> foodDtoList = FoodDto.readFromExcel(file.getInputStream().toString());
            List<Food> foodEntities = new ArrayList<>();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+foodDtoList);

            for (FoodDto foodDto : foodDtoList) {
                foodEntities.add(foodDto.toFoodEntity());
            }

            foodRepository.saveAll(foodEntities);
        } catch (IOException e) {
            // 예외 처리 로직을 여기에 추가
            e.printStackTrace();
            // 혹은 로그에 기록하거나, 사용자에게 메시지를 전달하는 등의 처리를 수행할 수 있습니다.
        }
    }
}


