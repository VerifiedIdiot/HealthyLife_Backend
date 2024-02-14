package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;

import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import com.HealthCare.HealthyLife_Backend.repository.MealRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;


    @Transactional
    public void addAndUpdateCalendar(MealDto mealDto) {
        // 이메일을 통해서 member 엔티티 조회
        Member member = memberRepository.findByEmail(mealDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with email: " + mealDto.getEmail()));

        // MealDto로부터 Meal 엔티티 변환
        Meal meal = mealDto.toMealEntity();


        // 음식명을 사용하여 Food 엔티티 조회
        Food food = foodRepository.findByName(mealDto.getMealName())
                .orElseThrow(() -> new EntityNotFoundException("Food not found with name: " + mealDto.getMealName()));
        meal.setFood(food);

        // Meal의 regDate를 기반으로 Calendar 엔티티 찾기 또는 생성
        String regDate = meal.getRegDate();

        Calendar calendar = calendarRepository.findByRegDateAndMemberEmail(regDate, mealDto.getEmail())
                .orElseGet(() -> {
                    Calendar newCalendar = new Calendar();
                    newCalendar.setRegDate(regDate);
                    newCalendar.setMember(member);
                    newCalendar.setCarbohydrate(0);
                    newCalendar.setProtein(0);
                    newCalendar.setFat(0);
                    newCalendar.setCalorie(0);
                    newCalendar.setPoints(0); // 초기 점수 설정
                    return newCalendar;
                });

        // 영양소 정보와 점수 업데이트
        updateNutritionalInfoAndPoints(calendar, meal, food);

        // Calendar 엔티티 업데이트
        calendarRepository.save(calendar);

        // Meal에 Calendar 설정 및 저장
        meal.setCalendar(calendar);
        mealRepository.save(meal);
    }

    private void updateNutritionalInfoAndPoints(Calendar calendar, Meal meal, Food food) {
        // 영양소 정보 업데이트
        calendar.setCarbohydrate(calendar.getCarbohydrate() + Float.parseFloat(food.getCarbohydrate()));
        calendar.setProtein(calendar.getProtein() + Float.parseFloat(food.getProtein()));
        calendar.setFat(calendar.getFat() + Float.parseFloat(food.getFat()));
        calendar.setCalorie(calendar.getCalorie() + Float.parseFloat(food.getKcal()));


        // 식사 타입별 달성 여부 확인 및 점수 업데이트
        updateMealAchievement(calendar, meal);
    }

    private void updateMealAchievement(Calendar calendar, Meal meal) {
        // 식사 타입에 따른 달성 여부 업데이트
        switch (meal.getMealType()) {
            case "아침":
                if (!calendar.getMorningMealAchieved()) {
                    calendar.setMorningMealAchieved(true);
                    calendar.setPoints(calendar.getPoints() + 25);
                }
                break;
            case "점심":
                if (!calendar.getLunchMealAchieved()) {
                    calendar.setLunchMealAchieved(true);
                    calendar.setPoints(calendar.getPoints() + 25);
                }
                break;
            case "저녁":
                if (!calendar.getDinnerMealAchieved()) {
                    calendar.setDinnerMealAchieved(true);
                    calendar.setPoints(calendar.getPoints() + 25);
                }
                break;
        }
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
        System.out.println(email);
        System.out.println(regDate);
        List<Meal> meals = mealRepository.findByEmailAndRegDate(email, regDate);

        System.out.println(meals);
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
