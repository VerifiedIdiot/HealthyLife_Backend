package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.TotalRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import com.HealthCare.HealthyLife_Backend.repository.TotalRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalRankingService {


    private final TotalRankingRepository totalRankingRepository;
//
//    public List<TotalRanking> getRankingSortedByPoints() {
//        return totalRankingRepository.findAll().stream()
//                .sorted(Comparator.comparingInt(TotalRanking::getPoints))
//                .collect(Collectors.toList());
//    }


    public List<TotalRankingDto> getTotalRankingList() {
        List<TotalRanking> totalRankings = totalRankingRepository.findTotalByOrderByPointsAsc();

        // TotalRanking 엔티티를 TotalRankingDto로 변환하여 리스트 생성
        List<TotalRankingDto> totalRankingDtos = new ArrayList<>();

        for (TotalRanking totalRanking : totalRankings) {
            TotalRankingDto totalRankingDto = totalRanking.toDto();
            totalRankingDtos.add(totalRankingDto);
        }
        return totalRankingDtos;
    }
}