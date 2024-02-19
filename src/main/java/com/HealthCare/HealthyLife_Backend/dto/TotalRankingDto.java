package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import lombok.*;

import java.util.List;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalRankingDto {

    private Long id;
    private Long memberId;
    private List<SeasonRanking> seasonRankings;
    private Integer points;



    public TotalRankingDto toEntity() {
        return TotalRankingDto.builder()
                .id(this.getId())
                .memberId(this.getMemberId())
                .seasonRankings(this.getSeasonRankings())
                .points(this.getPoints())
                .build();
    }

}



