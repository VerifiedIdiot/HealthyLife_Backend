package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface CalendarRepository extends JpaRepository<Calendar, Long> {
//    Boolean existByIdAndWriteDate(Long memberId, LocalDateTime regDate);
//    List<Calendar> findByMemberIdEmail(String email);

}