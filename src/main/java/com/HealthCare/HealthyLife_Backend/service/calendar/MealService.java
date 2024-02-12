package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import com.HealthCare.HealthyLife_Backend.repository.MealRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;


    public MealService(MealRepository mealRepository, FoodRepository foodRepository, MemberRepository memberRepository) {
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
        this.memberRepository = memberRepository;
    }

    public void addMealWithFood(MealDto mealDto) {
        Meal meal = mealDto.toMealEntity();
        mealRepository.save(meal);
    }

    // 키워드, 멤버ID, 유형, 날짜
//    public MealDto addMealWithFood(String keyword, String id, String type, String regDate) {
//        MealDto mealDto = new MealDto();
//
//        FoodDto foodDto = foodRepository.findByName(keyword);
//        System.out.println(foodDto);
//        mealDto.setMealName(foodDto.getName());
//        mealDto.setMemberId(id);
//        mealDto.setMealType(type);
//        mealDto.setRegDate(LocalDateTime.parse(regDate));
//        Meal meal = mealDto.toMealEntity();
//        mealRepository.save(meal);
//
//        return null;
//    }

    public List<FoodDto> getFoodKeyword(String keyword) {
        List<FoodDto> foodDtos = foodRepository.findAllByName(keyword);
        return foodDtos;
    }

    // 수정
    public ResponseEntity<String> modifyMeal(Long id, MealDto mealDto) {
        try {
            Meal meal = mealRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 식단이 존재하지 않습니다.")
            );
            meal.setMealType(mealDto.getMealType());
            meal.setMealName(mealDto.getMealName());
            mealRepository.save(meal);
            return ResponseEntity.ok("식단이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 출력
    public List<MealDto> getMealByEmail(String email, String regDate) {
        List<Meal> meals = mealRepository.findByEmailAndRegDate(email, regDate);
        return meals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

//    public List<MealDto> findByEmailAndRegDateAndMealNameAndMealType(
//            String email, String mealType, String mealName, String regDate) {
//        List<Meal> meals = mealRepository.findByEmailAndRegDate(email, regDate);
//        return meals.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    private MealDto convertToDto(Meal meal) {
        MealDto mealDto = new MealDto();
        mealDto.setId(meal.getId());
        mealDto.setMealName(meal.getMealName());
        mealDto.setMealType(meal.getMealType());
        mealDto.setEmail(meal.getEmail());
        mealDto.setRegDate(meal.getRegDate());
        return mealDto;
    }

    // 삭제
    public boolean deleteMeal(Long id) {
        try {
            mealRepository.findById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MealDto> findAll() {
        return mealRepository.findAll().stream()
                .map(Meal::toMealDto)
                .collect(Collectors.toList());
    }
}
