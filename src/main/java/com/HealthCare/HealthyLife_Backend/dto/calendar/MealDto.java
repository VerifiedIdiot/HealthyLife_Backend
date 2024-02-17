package com.HealthCare.HealthyLife_Backend.dto.calendar;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Meal;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonView(Views.Internal.class)
    private String mealType;

    @JsonView(Views.Internal.class)
    private String mealName;

    private String email;
    @JsonView(Views.Internal.class)
    private String regDate;

    public Meal toMealEntity() {
        return Meal.builder()
                .mealType(this.getMealType())
                .mealName(this.getMealName())
                .regDate(this.getRegDate())
                .build();
    }
}
