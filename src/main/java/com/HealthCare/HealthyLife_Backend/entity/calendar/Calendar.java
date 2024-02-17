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

@Builder
public class Calendar {
    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float carbohydrate; // 탄수화물
    private float protein; // 단백질
    private float fat; // 지방
    private float calorie; // 칼로리

    @Builder.Default
    @Column(name = "morning_meal_achieved", nullable = false)
    private Boolean morningMealAchieved = false;

    @Builder.Default
    @Column(name = "lunch_meal_achieved", nullable = false)
    private Boolean lunchMealAchieved = false;

    @Builder.Default
    @Column(name = "dinner_meal_achieved", nullable = false)
    private Boolean dinnerMealAchieved = false;

    @Builder.Default
    @Column(name = "workout_achieved", nullable = false)
    private Boolean workoutAchieved = false;

    @Builder.Default
    @Column(name = "calorie_over", nullable = false)
    private Boolean calorieOver = false;

    private Integer points;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email" ,referencedColumnName = "email")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private Body body;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workout> workout;

    private String regDate;
    // 리스트를 뿌리기 위한 간략한 정보
    public CalendarDto toCalendarDto() {
        return CalendarDto.builder()
                .calendarId(this.getId())
                .regDate(this.getRegDate())
                .morningMealAchieved(this.getMorningMealAchieved())
                .lunchMealAchieved(this.getLunchMealAchieved())
                .dinnerMealAchieved(this.getDinnerMealAchieved())
                .workoutAchieved(this.getWorkoutAchieved())
                .calorieOver(this.getCalorieOver())
                .calorie(this.getCalorie())
                .build();
    }
    // 리스트에서 하나의 항목을 선택시 상세 내용
    public CalendarDto toDtoWithDetail() {
        return CalendarDto.builder()
                .calorie(this.getCalorie())
                .carbohydrate(this.getCarbohydrate())
                .protein(this.getProtein())
                .fat(this.getFat())
                .build();
    }
}