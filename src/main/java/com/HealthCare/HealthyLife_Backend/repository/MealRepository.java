package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByMember_EmailAndRegDate(String email, String regDate);



//    List<MealDto> findByEmailAndRegDateAndMealNameAndMealType(String email, String mealType, String mealName, String regDate);
}
