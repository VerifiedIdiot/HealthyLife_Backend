package com.HealthCare.HealthyLife_Backend.controller.calendar;

import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.WorkoutDto;
import com.HealthCare.HealthyLife_Backend.service.calendar.WorkoutService;
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
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestBody(required = true) WorkoutDto workoutDto) {
        try {
            System.out.println(workoutDto);
            workoutService.addAndUpdateCalendar(workoutDto);
            return ResponseEntity.ok("운동추가");
        } catch (Exception e) {
            log.error("운동 추가 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



    // 운동 검색
    @JsonView(Views.Internal.class)
    @GetMapping("/view/search")
    public ResponseEntity<List<ExerciseDto>> getWorkoutKeyword(
            @RequestParam(required = true) String keyword) {
        try {
            List<ExerciseDto> exerciseDtos = workoutService.getWorkoutKeyword(keyword);
            return ResponseEntity.ok(exerciseDtos);
        } catch (Exception e) {
            log.error("운동 검색 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/findAll")
    public ResponseEntity<?> findAllData() {
        try {
            List<WorkoutDto> results = workoutService.findAll();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 조회 실패: " + e.getMessage());
        }
    }

    // 출력
    @GetMapping("/detail")
    public ResponseEntity<List<WorkoutDto>> MealByCalendarId(@RequestParam Long calendarId) {
        List<WorkoutDto> list = workoutService.getWorkoutByCalendarId(calendarId);
//        System.out.println("이메일 : " + email + " 날짜 :" + regDate );
        System.out.println(list);
        return ResponseEntity.ok(list);
    }
}
