package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.SeasonRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import com.HealthCare.HealthyLife_Backend.repository.SeasonRankingRepository;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeasonRankingService {

    private final SeasonRankingRepository seasonRankingRepository;

    public List<SeasonRankingDto> getSeasonRankingList(){
        List<SeasonRanking> seasonRankings = seasonRankingRepository.findSeasonByOrderByPointsAsc();
        List<SeasonRankingDto> seasonRankingDtos = new ArrayList<>();

        for (SeasonRanking seasonRanking : seasonRankings) {
            SeasonRankingDto seasonRankingDto = seasonRanking.toDto();
            seasonRankingDtos.add(seasonRankingDto);
        }
        return seasonRankingDtos;
    }
}
