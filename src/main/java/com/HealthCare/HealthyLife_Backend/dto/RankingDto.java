package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import lombok.*;


@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingDto {

    private Long id;

    private Member member;
    private Calendar calendar;
    private Long points;
    private Integer ranking;

    // SeasonRanking을 위한 팩토리 메서드
    public  SeasonRanking toSeasonEntity() {
        return SeasonRanking.builder()
                .id(this.getId())
                .member(this.getMember())
                .calendar(this.getCalendar())
                .points(this.getPoints())
                .ranking(this.getRanking())
                .build();
    }
}



