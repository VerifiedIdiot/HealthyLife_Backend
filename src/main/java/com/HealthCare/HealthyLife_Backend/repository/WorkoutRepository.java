package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
}
