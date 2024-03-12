package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;

import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.repository.*;
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
    private final BodyRepository bodyRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final TotalRankingRepository totalRankingRepository;



    @Transactional
    public Long addAndUpdateCalendar(MealDto mealDto) {
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
        String yearMonth = regDate.substring(0,6);



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


        Body latestBody = bodyRepository.findTopByMemberEmailOrderByDateDesc(mealDto.getEmail())
                .orElse(null); // 사용자의 최신 Body 정보 조회
        if (latestBody != null) {
            calendar.setBody(latestBody); // Calendar에 최신 Body 정보 설정
        }

        TotalRanking totalRanking = totalRankingRepository.findByMemberEmail(mealDto.getEmail())
                .orElseGet(() -> {
                    TotalRanking newTotalRanking = new TotalRanking();
                    newTotalRanking.setMember(member);
                    newTotalRanking.setPoints(0); // 초기 점수 설정
                    return newTotalRanking;
                });

        SeasonRanking seasonRanking = seasonRankingRepository.findByRegDateAndMemberEmail(yearMonth, mealDto.getEmail())
                .orElseGet(() -> {
                    SeasonRanking newSeasonRanking = new SeasonRanking();
                    newSeasonRanking.setRegDate(yearMonth);
                    newSeasonRanking.setMember(member);
                    newSeasonRanking.setPoints(0); // 초기 점수 설정

                    totalRanking.addSeasonRanking(newSeasonRanking);

                    return newSeasonRanking;
                });





        // 영양소 정보와 점수 업데이트
        updateNutritionalInfoAndPoints(calendar, meal, food, seasonRanking, totalRanking);

        if (latestBody != null) {
            calendar.setBody(latestBody); // Calendar에 최신 Body 정보 설정
            // Body의 dci 값을 float로 변환 (dci가 문자열로 저장된 경우)
            float dciValue = Float.parseFloat(latestBody.getDci());
            // 칼로리가 dci를 초과하는지 여부 확인 및 calorieOver 설정
            calendar.setCalorieOver(calendar.getCalorie() > dciValue);
        }


        // Calendar 엔티티 업데이트
        calendarRepository.save(calendar);
        seasonRankingRepository.save(seasonRanking);
        totalRankingRepository.save(totalRanking);


        // Meal에 Calendar 설정 및 저장
        meal.setCalendar(calendar);
        mealRepository.save(meal);

        return calendar.getId();
    }

    private void updateNutritionalInfoAndPoints(Calendar calendar, Meal meal, Food food,
                                                SeasonRanking seasonRanking, TotalRanking totalRanking) {
        // 영양소 정보 업데이트
        calendar.setCarbohydrate(calendar.getCarbohydrate() + Float.parseFloat(food.getCarbohydrate()));
        calendar.setProtein(calendar.getProtein() + Float.parseFloat(food.getProtein()));
        calendar.setFat(calendar.getFat() + Float.parseFloat(food.getFat()));
        calendar.setCalorie(calendar.getCalorie() + Float.parseFloat(food.getKcal()));


        // 식사 타입별 달성 여부 확인 및 점수 업데이트
        updateMealAchievement(calendar, meal, seasonRanking, totalRanking);
    }

    private void updateMealAchievement(Calendar calendar, Meal meal, SeasonRanking seasonRanking, TotalRanking totalRanking) {
        // 식사 타입에 따른 달성 여부 업데이트
        switch (meal.getMealType()) {
            case "아침":
                if (!calendar.getMorningMealAchieved()) {
                    calendar.setMorningMealAchieved(true);
                    calendar.setPoints(calendar.getPoints() + 25);
                    seasonRanking.setPoints(seasonRanking.getPoints() + 25);
                    totalRanking.setPoints(totalRanking.getPoints() + 25);
                }
                break;
            case "점심":
                if (!calendar.getLunchMealAchieved()) {
                    calendar.setLunchMealAchieved(true);
                    calendar.setPoints(calendar.getPoints() + 25);
                    seasonRanking.setPoints(seasonRanking.getPoints() + 25);
                    totalRanking.setPoints(totalRanking.getPoints() + 25);
                }
                break;
            case "저녁":
                if (!calendar.getDinnerMealAchieved()) {
                    calendar.setDinnerMealAchieved(true);
                    calendar.setPoints(calendar.getPoints() + 25);
                    seasonRanking.setPoints(seasonRanking.getPoints() + 25);
                    totalRanking.setPoints(totalRanking.getPoints() + 25);
                }
                break;
        }
    }


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
    public List<MealDto> getMealByCalendarId(Long calendarId) {

        List<Meal> meals = mealRepository.findByCalendarId(calendarId);
        return meals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MealDto convertToDto(Meal meal) {
        MealDto mealDto = new MealDto();
        mealDto.setId(meal.getId());
        mealDto.setMealName(meal.getMealName());
        mealDto.setMealType(meal.getMealType());
        mealDto.setRegDate(meal.getRegDate());

        // Food 엔티티에서 정보 가져오기
        if (meal.getFood() != null) {
            mealDto.setKcal(meal.getFood().getKcal());
            mealDto.setProtein(meal.getFood().getProtein());
            mealDto.setFat(meal.getFood().getFat());
            mealDto.setCarbohydrate(meal.getFood().getCarbohydrate());
        }

        return mealDto;
    }



    // 식사기록 삭제 & 점수차감
    @Transactional
    public boolean deleteMeal(Long id) {
        try {
            Meal meal = mealRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + id));

            Calendar calendar = meal.getCalendar();
            String mealType = meal.getMealType();
            Long calendarId = calendar.getId();

            mealRepository.delete(meal); // 식사 기록 삭제

            // 남은 식사 기록 확인
            long remainingMealsCount = mealRepository.countByCalendarIdAndMealType(calendarId, mealType);

            if (remainingMealsCount == 0) { // 더 이상 해당 유형의 식사 기록이 없다면
                updatePointsAndAchievement(calendar, mealType, false); // 점수 차감 및 달성 여부 업데이트
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updatePointsAndAchievement(Calendar calendar, String mealType, boolean achieved) {
        // 식사 유형에 따른 달성 여부 및 점수 업데이트 로직
        switch (mealType) {
            case "아침":
                if (calendar.getMorningMealAchieved() && !achieved) {
                    calendar.setMorningMealAchieved(false);
                    calendar.setPoints(calendar.getPoints() - 25);
                }
                break;
            case "점심":
                if (calendar.getLunchMealAchieved() && !achieved) {
                    calendar.setLunchMealAchieved(false);
                    calendar.setPoints(calendar.getPoints() - 25);
                }
                break;
            case "저녁":
                if (calendar.getDinnerMealAchieved() && !achieved) {
                    calendar.setDinnerMealAchieved(false);
                    calendar.setPoints(calendar.getPoints() - 25);
                }
                break;
        }

        // 엔티티 저장
        calendarRepository.save(calendar);
    }


    public List<MealDto> findAll() {
        return mealRepository.findAll().stream()
                .map(Meal::toMealDto)
                .collect(Collectors.toList());
    }
}
