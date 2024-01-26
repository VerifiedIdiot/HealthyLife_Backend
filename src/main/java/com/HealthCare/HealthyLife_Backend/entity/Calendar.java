package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.CalendarDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "calendar_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Calendar {
    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "calendar_title",nullable = false)
    private String calendarTitle; //제목

    @Column(name = "calendar_detail",length = 1000)
    private String calendarDetail; // 내용

    @Column(name = "calendar_write_date",nullable = false)
    private LocalDate diaryWriteDate; //작성일자

    private int month;
    @Column(nullable = false)
    private String title; // 제목
    @Column(length = 1000)
    private String detail; // 내용
    @Column(name = "write_date", nullable = false)
    private LocalDate writeDate; // 작성일자

    private String morning; // 아침
    private String lunch; // 점심
    private String dinner; // 저녁
    private String workout; // 운동

    private Integer carbohydrate; // 탄수화물
    private Integer protein; // 단백질
    private Integer province; // 지방
    private Integer calorie; // 칼로리
    private Integer proportion; // 비율

    private Integer points; // 포인트


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private Body body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings;

    public CalendarDto toCalendarDto() {
        return CalendarDto.builder()
                .id(this.getId())
                .month(this.getMonth())
                .title(this.getTitle())
                .detail(this.getDetail())
                .writeDate(this.getWriteDate())
                .points(this.getPoints())
                .build();
    }
}
