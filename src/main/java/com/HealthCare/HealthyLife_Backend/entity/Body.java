package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "body_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Body {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @PrePersist
    protected void prePersist() {
        date = LocalDate.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", referencedColumnName = "email")
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

    @Column(name = "DCI")
    private String DCI;

    public BodyDto toBodyDto() {
        return BodyDto.builder()
                .date(this.getDate())
                .height(this.getHeight())
                .weight(this.getWeight())
                .muscle(this.getMuscle())
                .fat(this.getFat())
                .fatPercent(this.getFatPercent())
                .bmr(this.getBmr())
                .bmi(this.getBmi())
                .DCI(this.getDCI())
                .build();
    }
}