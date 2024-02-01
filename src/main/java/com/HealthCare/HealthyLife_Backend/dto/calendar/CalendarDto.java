package com.HealthCare.HealthyLife_Backend.dto.calendar;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CalendarDto {

    private Long calendarId;
    private int month;
    private String title; //제목
    private String detail; // 내용
    private LocalDateTime regDate; // 작성일자

    private Integer carbohydrate; // 탄수화물
    private Integer protein; // 단백질
    private Integer fat; // 지방
    private Integer calorie; // 칼로리
    private Long memberId;

    private Integer points; // 포인트

    public Calendar toCalendarEntity() {
        return Calendar.builder()
                .regDate(this.getRegDate())
                .points(this.getPoints())
                .build();
    }
}