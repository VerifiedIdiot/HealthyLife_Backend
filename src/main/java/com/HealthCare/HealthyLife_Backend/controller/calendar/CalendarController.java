package com.HealthCare.HealthyLife_Backend.controller.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.service.calendar.CalendarService;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController  {

    private final CalendarService calendarService;

    @GetMapping("/test")
    public ResponseEntity<?> testController () {
        try {
            System.out.println("확인");
            // return ResponseEntity.ok("Food search");
        } catch (Exception e) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("정상작동");
    }

    @JsonView(Views.List.class)
    @GetMapping("/get-month-details")
    public ResponseEntity<?> getMonthDetails(@RequestParam String month,
                                             @RequestParam String email) {
        try {
            // calendarService를 통해 해당 년도와 월에 해당하는 데이터 가져오기
            List<CalendarDto> calendarDtos = calendarService.findByYearAndMonth(email, month);
            return ResponseEntity.ok(calendarDtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    // 수정해야함 이 부분
    @JsonView(Views.Detail.class)
    @GetMapping("/get-date-details")
    public ResponseEntity<?> getDateDetails(@RequestParam Long calendarId) {
        try {
            // calendarService를 통해 해당 년도와 월에 해당하는 데이터 가져오기
            CalendarDto calendarDto = calendarService.findByDate(calendarId);
            return ResponseEntity.ok(calendarDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<CalendarDto>> findAll() {

        try {
            List<CalendarDto> calendars = calendarService.findAll();
            return ResponseEntity.ok(calendars);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<CalendarDto> findById(Long id) {
        try {
            CalendarDto calendar = calendarService.findById(id);
            return calendar != null ? ResponseEntity.ok(calendar) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            calendarService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CalendarDto> update(@PathVariable Long id, @RequestBody CalendarDto calendarDto) {
        try {
            CalendarDto updatedCalendar = calendarService.update(id, calendarDto);
            return ResponseEntity.ok(updatedCalendar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
