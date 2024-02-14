package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import com.HealthCare.HealthyLife_Backend.repository.MealRepository;
import com.HealthCare.HealthyLife_Backend.repository.WorkoutRepository;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import com.HealthCare.HealthyLife_Backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final MealService mealService;
    private final CalendarRepository calendarRepository;

    public List<CalendarDto> findByYearAndMonth(String email, String regDate) {
        // 202402% 와 , 이메일정보로 리스트 반환
        System.out.println(regDate + email);
        // regDate 형식 YYYYmm
        return calendarRepository.findByRegDateLikeAndMemberEmail(regDate +"%", email);
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