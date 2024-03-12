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
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CalendarDto {


    private Long calendarId;



    private float calorie;


    private float carbohydrate;


    private float protein;


    private float fat;

    private String dci;


    private Boolean morningMealAchieved;


    private Boolean lunchMealAchieved;


    private Boolean dinnerMealAchieved;


    private Boolean workoutAchieved;


    private Boolean calorieOver;


    private String regDate;

}