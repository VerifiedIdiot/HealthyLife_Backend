package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import com.HealthCare.HealthyLife_Backend.repository.MealRepository;
import com.HealthCare.HealthyLife_Backend.repository.WorkoutRepository;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import com.HealthCare.HealthyLife_Backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    @Autowired
    private final CalendarRepository calendarRepository;
    private final MealRepository mealRepository;
    private final WorkoutRepository workoutRepository;

    private final MemberService memberService;
    private final FoodService foodService;
    private final FoodRepository foodRepository;


    public List<CalendarDto> findAll() {
        return null;
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
}