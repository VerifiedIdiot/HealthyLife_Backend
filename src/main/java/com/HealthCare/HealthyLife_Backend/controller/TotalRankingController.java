package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.TotalRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import com.HealthCare.HealthyLife_Backend.repository.TotalRankingRepository;
import com.HealthCare.HealthyLife_Backend.service.TotalRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/totalRanking")
public class TotalRankingController {

    private final TotalRankingService totalRankingService;
    private final TotalRankingRepository totalRankingRepository;


    @GetMapping("/test")
    public ResponseEntity<?> testController() {
        try {
            System.out.println("확인");
        } catch (Exception e) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("정상작동");
    }

//    @GetMapping("/detail")
//    public ResponseEntity<?> totalByMemberEmail() {
//        try {
//            List<TotalRankingDto> totalRankingDtos = totalRankingService.getTotalRankingList();
//            return ResponseEntity.ok(totalRankingDtos);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/detail")
    public ResponseEntity<?> totalByMemberEmail() {
        try {
            List<TotalRankingDto> totalRankingDtos = totalRankingService.getTotalRankingList();
            System.out.println(totalRankingDtos);
            return ResponseEntity.ok(totalRankingDtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}