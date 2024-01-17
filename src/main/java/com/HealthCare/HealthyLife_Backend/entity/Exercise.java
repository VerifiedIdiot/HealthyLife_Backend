package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "exercise") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "instructions")
    private String instructions;

}
