package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import com.HealthCare.HealthyLife_Backend.repository.MealRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {
    private MealRepository mealRepository;
    private FoodRepository foodRepository;
    private MemberRepository memberRepository;

    private CalendarRepository calendarRepository;



    @Transactional
    public void addAndUpdateCalendar(MealDto mealDto) {
        // MealDto로부터 Meal 엔티티 변환 (mealDto.toMealEntity() 같은 메서드 필요)
        Meal meal = mealDto.toMealEntity(); // 이 메서드는 MealDto를 Meal 엔티티로 변환

        // Meal의 regDate를 기반으로 Calendar 엔티티 찾기
        String regDate = meal.getRegDate();
        Calendar calendar = calendarRepository.findByRegDateAndMemberEmail(regDate, meal.getMember().getEmail())
                .orElseGet(() -> {
                    // 새 Calendar 엔티티 생성 및 초기화
                    Calendar newCalendar = new Calendar();
                    newCalendar.setRegDate(regDate);
                    newCalendar.setMember(meal.getMember()); // Meal과 같은 Member 할당
                    // 필요한 경우 추가적인 Calendar 초기화
                    return calendarRepository.save(newCalendar); // 새 Calendar 저장
                });

        // Meal에 Calendar 설정
        meal.setCalendar(calendar);

        // Meal 저장
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
        System.out.println(email);
        System.out.println(regDate);
        List<Meal> meals = mealRepository.findByMember_EmailAndRegDate(email, regDate);

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
        mealDto.setMember(meal.getMember());
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
