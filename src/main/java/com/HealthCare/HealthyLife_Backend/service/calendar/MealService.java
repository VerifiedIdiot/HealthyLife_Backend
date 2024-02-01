package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import com.HealthCare.HealthyLife_Backend.repository.MealRepository;
import org.springframework.stereotype.Service;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;



    public MealService(MealRepository mealRepository, FoodRepository foodRepository) {
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;


    }

    public MealDto addMealWithFood(String keyword, String id, String type ) {
        MealDto mealDto = new MealDto();

        FoodDto foodDto = foodRepository.findByName(keyword);
        System.out.println(foodDto);
        mealDto.setMealName(foodDto.getName());
        mealDto.setMemberId(id);
        mealDto.setMealType(type);
        Meal meal = mealDto.toMealEntity();
        mealRepository.save(meal);

        return null;
    }
}
