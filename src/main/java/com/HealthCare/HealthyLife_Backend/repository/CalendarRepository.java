package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByRegDateAndMemberEmail(String regDate, String email);

    List<CalendarDto> findByRegDateLikeAndMemberEmail(String regDate, String email);

    @Query("SELECT c FROM Calendar c WHERE c.id = :id")
    List<Calendar> findByCalendarId(@Param("id") Long id);

    List<Calendar> findByMember(Member member);


    //    Boolean existByIdAndWriteDate(Long memberId, LocalDateTime regDate);
//    List<Calendar> findByMemberIdEmail(String email);

}