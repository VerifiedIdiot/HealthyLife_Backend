package com.HealthCare.HealthyLife_Backend.dto.calendar;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Workout;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WorkoutDto {
    @JsonView(Views.Internal.class)
    private Long id;
    @JsonView(Views.Internal.class)
    private String workoutName;

    private String email;

    @JsonView(Views.Internal.class)
    private String regDate;

    public Workout toWorkoutEntity() {
        return Workout.builder()
                .workoutName(this.getWorkoutName())
                .email(this.getEmail())
                .regDate(this.getRegDate())
                .build();
    }
}
