package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.service.BodyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Body")
@RequiredArgsConstructor
public class BodyController {
    private final BodyService bodyService;

//    @GetMapping("/list")
//    public ResponseEntity<List<BodyDto>> bodyList() {
//        List<BodyDto> list = BodyService.getBodyList();
//        return ResponseEntity.ok(list);
//    }

    @GetMapping("/list/email")
    public ResponseEntity<List<BodyDto>> bodyByEmail(@RequestParam String email) {
        List<BodyDto> list = bodyService.getBodyByEmail(email);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/new")
    public ResponseEntity<Boolean> BodyRegister(@RequestBody BodyDto bodyDto) {
        boolean isTrue = bodyService.saveBody(bodyDto);
        return ResponseEntity.ok(isTrue);
    }
}
