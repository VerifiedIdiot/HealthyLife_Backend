package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Body") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Body {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private String weight;

    @Column(name = "muscle")
    private String muscle;

    @Column(name = "fat")
    private String fat;

    @Column(name = "fat_percent")
    private String fatPercent;

    @Column(name = "bmr")
    private String bmr;

    @Column(name = "bmi")
    private String bmi;
}