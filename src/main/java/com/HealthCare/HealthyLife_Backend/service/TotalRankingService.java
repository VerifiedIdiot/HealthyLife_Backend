package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.TotalRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import com.HealthCare.HealthyLife_Backend.repository.TotalRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalRankingService {


    private final TotalRankingRepository totalRankingRepository;


    public List<TotalRankingDto> getTotalRankingList() {
        List<TotalRanking> totalRankings = totalRankingRepository.findAll();

        // 포인트를 기준으로 내림차순 정렬
        List<TotalRankingDto> sortedRankings = totalRankings.stream()
                .map(totalRanking -> TotalRankingDto.builder()
                        .id(totalRanking.getId())
                        .memberId(totalRanking.getMember().getId())
                        .points(totalRanking.getPoints())
                        .nickname(totalRanking.getMember().getNickName())
                        .gender(totalRanking.getMember().getGender())
                        .build())
                .sorted(Comparator.comparingInt(TotalRankingDto::getPoints).reversed())
                .collect(Collectors.toList());


        int currentRank = 1;
        Integer previousPoints = null;
        int nextRank = 1;

        for (TotalRankingDto rankingDto : sortedRankings) {
            if (previousPoints == null || !rankingDto.getPoints().equals(previousPoints)) {
                currentRank = nextRank;
            }
            rankingDto.setRanks(currentRank);

            if (previousPoints == null || !rankingDto.getPoints().equals(previousPoints)) {
                previousPoints = rankingDto.getPoints();
            }

            nextRank++;
        }

        return sortedRankings;
    }

}