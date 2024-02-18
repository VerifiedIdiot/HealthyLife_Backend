package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalRankingDto {

    private Long id;
    private Member member;
    private List<SeasonRanking> seasonRankings;
    private Integer points;



    public TotalRankingDto toEntity() {
        return TotalRankingDto.builder()
                .id(this.getId())
                .member(this.getMember())
                .seasonRankings(this.getSeasonRankings())
                .points(this.getPoints())
                .build();
    }

}



