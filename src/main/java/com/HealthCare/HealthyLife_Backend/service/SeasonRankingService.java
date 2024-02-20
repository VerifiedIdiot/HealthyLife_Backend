package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.SeasonRankingDto;
import com.HealthCare.HealthyLife_Backend.dto.TotalRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import com.HealthCare.HealthyLife_Backend.repository.SeasonRankingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeasonRankingService {

    private final SeasonRankingRepository seasonRankingRepository;
    private final MemberRepository memberRepository;



    public List<SeasonRankingDto> getMemberPointsForCurrentMonth() {

        YearMonth currentYearMonth = YearMonth.now();
        String yearMonth = currentYearMonth.toString().replace("-", "");

        List<SeasonRanking> seasonRankings = seasonRankingRepository.findByRegDateStartingWithOrderByPointsDesc(yearMonth);

        int rank = 1;
        int counter = 1;
        Integer previousPoints = null;

        for (int i = 0; i < seasonRankings.size(); i++) {
            SeasonRanking seasonRanking = seasonRankings.get(i);


            if (previousPoints == null || !seasonRanking.getPoints().equals(previousPoints)) {
                rank = counter;
            }

            seasonRanking.setRanks(rank);

            previousPoints = seasonRanking.getPoints();
            counter++;
        }

        // DTO로 변환하여 반환
        return seasonRankings.stream()
                .map(seasonRanking -> SeasonRankingDto.builder()
                        .id(seasonRanking.getId())
                        .memberId(seasonRanking.getMember().getId())
                        .points(seasonRanking.getPoints())
                        .nickname(seasonRanking.getMember().getNickName())
                        .gender(seasonRanking.getMember().getGender())
                        .regDate(seasonRanking.getRegDate())
                        .ranks(seasonRanking.getRanks())
                        .build())
                .collect(Collectors.toList());
    }
}
