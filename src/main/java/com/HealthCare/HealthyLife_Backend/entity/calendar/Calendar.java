package com.HealthCare.HealthyLife_Backend.entity.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.CalendarDto;
import com.HealthCare.HealthyLife_Backend.entity.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "calendar_tb")
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

    private int month;

    private Integer carbohydrate; // 탄수화물
    private Integer protein; // 단백질
    private Integer fat; // 지방
    private Integer calorie; // 칼로리

    private Integer points; // 포인트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email" ,referencedColumnName = "email")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private Body body;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workout> workout;

    private String regDate;


    public CalendarDto toCalendarDto() {
        return CalendarDto.builder()
                .regDate(this.getRegDate())
                .points(this.getPoints())
                .build();
    }
}