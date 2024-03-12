package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.repository.BodyRepository;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final BodyRepository bodyRepository;


    private final Logger log = LoggerFactory.getLogger(CalendarService.class);


    public List<CalendarDto> findByYearAndMonth(String email, String regDate) {
        List<Calendar> calendars = calendarRepository.findByRegDateLikeAndMemberEmail(regDate + "%", email);
        List<CalendarDto> results = calendars.stream()
                .map(calendar -> {
                    LocalDate calendarDate = convertStringToLocalDate(calendar.getRegDate());
                    Body latestBody = findLatestBodyBeforeDate(email, calendarDate).orElse(null);
                    return calendar.toCalendarDto(latestBody);
                })
                .collect(Collectors.toList());
        log.info("findByYearAndMonth results: {}", results);
        return results;
    }

    public CalendarDto findByDate(Long calendarId) {
        Optional<Calendar> calendar = calendarRepository.findById(calendarId);
        return calendar.map(Calendar::toDtoWithDetail).orElseThrow(() -> new EntityNotFoundException("Calendar not found with name: " + calendarId));
    }


    public LocalDate convertStringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // Ensure this matches your regDate format.
        return LocalDate.parse(dateStr, formatter);
    }

    public Optional<Body> findLatestBodyBeforeDate(String email, LocalDate date) {
        return bodyRepository.findTopByMemberEmailAndDateBeforeOrderByDateDesc(email, date);
    }


    // 기존의 dci(권장섭취량)을 바탕으로 true or false 업데이트 되지않은 데이터들을 조회하여 업데이트
    @Transactional
    public void updateCalorieOverForAllCalendars() {
        List<Calendar> calendars = calendarRepository.findAll(); // 모든 Calendar 레코드 조회

        for (Calendar calendar : calendars) {
            Body latestBody = calendar.getBody();
            if (latestBody != null && latestBody.getDci() != null) {
                try {
                    float dciValue = Float.parseFloat(latestBody.getDci()); // dci 값을 float로 변환
                    calendar.setCalorieOver(calendar.getCalorie() > dciValue); // calorieOver 재계산
                } catch (NumberFormatException e) {
                    // dci 값이 숫자로 변환될 수 없는 경우, 에러 처리 (예: 로깅)
                    e.printStackTrace();
                }
                calendarRepository.save(calendar); // 각 Calendar 레코드 업데이트
            }
        }
    }
}