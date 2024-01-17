package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.CalendarDto;
import com.HealthCare.HealthyLife_Backend.entity.Calendar;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    public List<CalendarDto> getMonthDataOrderBydate(int month) {
        List<CalendarDto> calendarDtos = new ArrayList<>();
        List<Calendar> calendars = calendarRepository.findByMonth(month);

        for (Calendar calendar : calendars) {
            CalendarDto calendarDto = calendar.toCalendarDto();
        }
        return calendarDtos;
    }
}