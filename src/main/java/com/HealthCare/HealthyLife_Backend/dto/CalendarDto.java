package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Calendar;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDto {
    private Long id;
    private String title; //제목
    private String detail; // 내용
    private LocalDate writeDate; //작성일자
    private Integer points; //포인트

    public Calendar toCalendarEntity() {
        return Calendar.builder()
                .id(this.getId())
                .title(this.getTitle())
                .detail(this.getDetail())
                .writeDate(this.getWriteDate())
                .points(this.getPoints())
                .build();
    }
}