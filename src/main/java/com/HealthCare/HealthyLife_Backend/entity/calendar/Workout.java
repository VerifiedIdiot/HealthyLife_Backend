package com.HealthCare.HealthyLife_Backend.entity.calendar;

import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Workout {

    @Id
    @Column(name = "workout_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="exercise_id")
    private Exercise exercise;
    @Column(name = "workout_name")
    private String workoutName;

    @Column(name = "email")
    private String email;
    private String regDate;

    public Workout toWorkoutEntity() {
        return Workout.builder()
                .workoutName(this.getWorkoutName())
                .email(this.getEmail())
                .regDate(this.getRegDate())
                .build();
    }



}
