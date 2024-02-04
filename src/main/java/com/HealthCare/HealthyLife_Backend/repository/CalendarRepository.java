package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    @Query("SELECT c FROM Calendar c WHERE c.month = :month")
    List<Calendar> findByMonth(@Param("month") int month);

}