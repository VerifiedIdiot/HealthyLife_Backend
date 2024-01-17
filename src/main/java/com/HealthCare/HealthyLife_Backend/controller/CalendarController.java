package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;


import com.HealthCare.HealthyLife_Backend.dto.CalendarDto;
import com.HealthCare.HealthyLife_Backend.service.CalendarService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController implements CrudControllerInterface<CalendarDto, Long> {

    private final CalendarService calendarService;


    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;

    }


    @Override
    public ResponseEntity<?> insert() {
        try {
//            Map<String, String> funtionalities = calendarService.getFunctionalities();
//            Map<String, String> medicineList = calendarService.getMedicineList();
            return ResponseEntity.ok("건강기능식품 insert");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }



    @Override
    public ResponseEntity<List<CalendarDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<CalendarDto> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<CalendarDto> update(Long id, CalendarDto calendarDto) {
        return null;
    }


    public ResponseEntity<List<CalendarDto>> getMonthData (@RequestParam int month) {
        try {
            List<CalendarDto> calendarDtos = calendarService.getMonthDataOrderBydate(month);
            return ResponseEntity.ok(calendarDtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}