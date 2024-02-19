package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonRankingDto {

    private Long id;
    private Long memberId;
    private List<TotalRanking> totalRankings;
    private Integer points;
    private String regDate;

    public TotalRankingDto toEntity() {
        return TotalRankingDto.builder()
                .id(this.getId())
                .memberId(this.getMemberId())
                .points(this.getPoints())

                .build();
    }
}


