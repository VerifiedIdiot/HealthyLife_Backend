package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import lombok.*;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalRankingDto {

    private Long id;
    private Long memberId;
    private Integer ranks;
    private String nickname;
    private String gender;
    private List<SeasonRanking> seasonRankings;
    private Integer points;


}



