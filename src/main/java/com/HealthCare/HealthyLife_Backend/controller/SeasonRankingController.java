package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.repository.SeasonRankingRepository;
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

    private final SeasonRankingRepository seasonRankingRepository;

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
    public List<SeasonRanking> seasonByMemberEmail() {

        return seasonRankingRepository.findSeasonByOrderByPointsAsc();
//                .map(seasonRanking -> ResponseEntity.ok(seasonRanking))
//                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
