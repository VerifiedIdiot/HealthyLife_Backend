package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import lombok.*;

import java.time.LocalDate;


@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingDto {

    private Long id;
    private String nickName;
    private String gender;
    private Long points;
    private Integer ranking;
    private Integer season; // 시즌 정보는 SeasonRanking에만 해당됩니다.
    private LocalDate seasonEndDate; // 시즌 종료 날짜는 SeasonRanking에만 해당됩니다.

    // DTO 빌더


    // TotalRanking을 위한 팩토리 메서드
    public TotalRanking toTotalEntity() {
        return TotalRanking.builder()
                .id(this.getId())
                .nickName(this.getNickName())
                .gender(this.getGender())
                .points(this.getPoints())
                .ranking(this.getRanking())
                .build();

    }


    // SeasonRanking을 위한 팩토리 메서드
    public  SeasonRanking toSeasonEntity() {
        return SeasonRanking.builder()
                .id(this.getId())
                .nickName(this.getNickName())
                .gender(this.getGender())
                .points(this.getPoints())
                .ranking(this.getRanking())
                .season(this.getSeason())
                .seasonEndDate(this.getSeasonEndDate())
                .build();
    }
}



