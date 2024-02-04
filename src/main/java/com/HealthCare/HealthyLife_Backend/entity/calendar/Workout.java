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

    private String workout;
    private String memberId;
    private LocalDateTime regDate;

    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
}
