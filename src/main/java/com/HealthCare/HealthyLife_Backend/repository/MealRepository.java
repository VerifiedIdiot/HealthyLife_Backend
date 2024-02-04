package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {

}
