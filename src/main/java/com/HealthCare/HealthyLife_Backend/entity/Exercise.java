package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "exercise_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "muscle")
    private String muscle;

    @Column(name = "equipment")
    private String equipment;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "instructions", length = 5000)
    private String instructions;

    @Column(name = "image")
    private String image;

    public ExerciseDto toExerciseDto() {
        return ExerciseDto.builder()
                .name(this.getName())
                .type(this.getType())
                .muscle(this.getMuscle())
                .equipment(this.getEquipment())
                .difficulty(this.getDifficulty())
                .instructions(this.getInstructions())
                .image(this.getImage())
                .build();
    }

}