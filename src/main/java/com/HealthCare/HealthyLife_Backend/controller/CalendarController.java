package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.service.CalendarService;
import com.HealthCare.HealthyLife_Backend.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/calendar")
public class CalendarController implements CrudControllerInterface<CalendarDto, Long> {

    private final CalendarService calendarService;
    private final FoodService foodService;







    public CalendarController(CalendarService calendarService, FoodService foodService) {
        this.calendarService = calendarService;
        this.foodService = foodService;
    }

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

    @PostMapping("/create/food")
    public ResponseEntity<List<FoodDto>> getFoodList() {
        List<FoodDto> foodList = foodService.getFoodList(0,10);
        try {
            foodList = foodService.getFoodList(0, 10);
            System.out.println(foodList);
            return ResponseEntity.ok(foodList);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/search{foodid}")
//    public ResponseEntity<List<FoodDto>> get

    @GetMapping("/findAll")
    public ResponseEntity<List<CalendarDto>> findAll() {
        return null;
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<CalendarDto> findById(Long id) {
        return null;
    }

    @GetMapping("/insert")
    public ResponseEntity<?> insert() {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<CalendarDto> update(Long id, CalendarDto calendarDto) {
        return null;
    }


}

// 엔드포인트를 추가하여 항목을 받아들일 수 있게 함