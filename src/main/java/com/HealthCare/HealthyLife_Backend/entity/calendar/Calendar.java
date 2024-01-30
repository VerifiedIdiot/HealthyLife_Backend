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

    private String workout; // 운동.

    private Integer carbohydrate; // 탄수화물
    private Integer protein; // 단백질
    private Integer fat; // 지방
    private Integer calorie; // 칼로리

    private Integer points; // 포인트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<Breakfast> breakfasts;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<Lunch> lunches;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<Dinner> dinners;

    private LocalDateTime regDate;
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

    public CalendarDto toCalendarDto() {
        return CalendarDto.builder()
                .month(this.getMonth())
                .regDate(this.getRegDate())
                .points(this.getPoints())
                .build();
    }
}
