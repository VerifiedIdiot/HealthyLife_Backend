package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.dto.calendar.WorkoutDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByCalendarId(Long calendarId);
}
