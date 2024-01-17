package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
    private Long calendarId;
    private String calendarTitle; //제목
    private String calendarDetail; // 내용
    private LocalDate calendarWriteDate; //작성일자
    private Integer points;
}