package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import lombok.*;


@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingDto {

    private Long id;

    private Calendar calendar;
    private Integer points;
    private String regDate;

    // SeasonRanking을 위한 팩토리 메서드
    public  SeasonRanking toSeasonEntity() {
        return SeasonRanking.builder()
                .id(this.getId())
                .calendar(this.getCalendar())
                .points(this.getPoints())
                .build();
    }
}



