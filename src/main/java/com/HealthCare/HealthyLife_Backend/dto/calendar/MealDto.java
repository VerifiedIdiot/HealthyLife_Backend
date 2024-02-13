package com.HealthCare.HealthyLife_Backend.dto.calendar;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MealDto {
    private Long id;
    private String mealType;
    private String mealName;
    private String email;
    private String regDate;

    public Meal toMealEntity() {
        return Meal.builder()
                .mealType(this.getMealType())
                .mealName(this.getMealName())
                .regDate(this.getRegDate())
                .build();
    }
}
