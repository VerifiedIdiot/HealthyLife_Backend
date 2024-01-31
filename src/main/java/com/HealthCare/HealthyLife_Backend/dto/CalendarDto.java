package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Calendar;
import lombok.*;
import java.time.LocalDate;

@Builder
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

    private Long id;
    private Integer month;
    private String title; //제목
    private String detail; // 내용
    private LocalDate writeDate; //작성일자

    private String morning; // 아침
    private String lunch; // 점심
    private String dinner; // 저녁
    private String workout; // 운동

    private Integer carbohydrate; // 탄수화물
    private Integer protein; // 단백질
    private Integer province; // 지방
    private Integer calorie; //칼로리
    private Integer proportion;// 비율

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