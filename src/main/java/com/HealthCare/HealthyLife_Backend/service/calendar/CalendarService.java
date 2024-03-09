package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class CalendarService {

    private final MealService mealService;
    private final CalendarRepository calendarRepository;

    private final Logger log = LoggerFactory.getLogger(CalendarService.class);


    public List<CalendarDto> findByYearAndMonth(String email, String regDate) {
        List<Calendar> calendars = calendarRepository.findByRegDateLikeAndMemberEmail(regDate + "%", email);
        List<CalendarDto> results = calendars.stream()
                .map(Calendar::toCalendarDto) // 여기서 상세 변환 메서드를 사용합니다.
                .collect(Collectors.toList());
        log.info("findByYearAndMonth results: {}", results);
        return results;
    }

    public CalendarDto findByDate(Long calendarId) {
        Optional<Calendar> calendar = calendarRepository.findById(calendarId);
        return calendar.map(Calendar::toDtoWithDetail).orElseThrow(() -> new EntityNotFoundException("Calendar not found with name: " + calendarId));
    }


    public void insert(CalendarDto calendarDto) {

    }

    public CalendarDto create(CalendarDto calendarDto) {
        return null;
    }

    public CalendarDto findById(Long id) {
        return null;
    }

    public CalendarDto update(Long id, CalendarDto calendarDto) {
        return null;
    }

    public void delete(Long id) {
    }

    public boolean saveCalendar() {
        return true;
    }

    public List<CalendarDto> findAll() {
        return null;
    }
}