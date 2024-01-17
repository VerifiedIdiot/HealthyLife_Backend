package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;

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

    @Column(name = "member_id")
    private String memberId;

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


}