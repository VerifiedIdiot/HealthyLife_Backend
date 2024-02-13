package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByRegDateAndMemberEmail(String regDate, String email);



    //    Boolean existByIdAndWriteDate(Long memberId, LocalDateTime regDate);
//    List<Calendar> findByMemberIdEmail(String email);

}