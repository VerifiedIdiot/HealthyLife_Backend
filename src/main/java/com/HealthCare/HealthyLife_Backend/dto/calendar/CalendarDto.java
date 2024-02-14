package com.HealthCare.HealthyLife_Backend.dto.calendar;

import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Workout;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CalendarDto {

    @JsonView(Views.List.class)
    private Long calendarId;


    @JsonView({Views.List.class, Views.Detail.class})
    private float calorie;

    @JsonView(Views.List.class)
    private Boolean morningMealAchieved;

    @JsonView(Views.List.class)
    private Boolean lunchMealAchieved;

    @JsonView(Views.List.class)
    private Boolean dinnerMealAchieved;

    @JsonView(Views.List.class)
    private Boolean workoutAchieved;

    @JsonView(Views.List.class)
    private Boolean calorieOver;

    @JsonView(Views.List.class)
    private String regDate;

    @JsonView(Views.Detail.class)
    private float carbohydrate;

    @JsonView(Views.Detail.class)
    private float protein;

    @JsonView(Views.Detail.class)
    private float fat;
}