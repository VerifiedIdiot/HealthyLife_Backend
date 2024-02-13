package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import lombok.*;

import java.sql.Clob;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExerciseDto {
    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;
    // builder를 통해서 반복된 getter setter 사용 방지 , @Query 어노테이션이랑 호환 안됨
    public Exercise toExerciseEntity() {
        return Exercise.builder()
                .name(this.name)
                .type(this.type)
                .muscle(this.muscle)
                .equipment(this.equipment)
                .difficulty(this.difficulty)
                .instructions(this.instructions)
                .build();
    }


}