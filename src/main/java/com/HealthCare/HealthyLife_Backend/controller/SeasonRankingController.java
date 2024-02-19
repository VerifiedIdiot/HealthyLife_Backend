package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.SeasonRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.repository.SeasonRankingRepository;
import com.HealthCare.HealthyLife_Backend.service.SeasonRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/seasonRanking")
public class SeasonRankingController {

    private final SeasonRankingService seasonRankingService;

    @GetMapping("/test")
    public ResponseEntity<?> testController () {
        try {
            System.out.println("확인");
        } catch (Exception e) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("정상작동");
    }

    // 출력
    @GetMapping("/detail")
    public ResponseEntity<?> seasonByMemberEmail() {
        try {
            List<SeasonRankingDto> seasonRankingDtos = seasonRankingService.getSeasonRankingList();
            return ResponseEntity.ok(seasonRankingDtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
