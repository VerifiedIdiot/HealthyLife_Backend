package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BodyDto {
    private LocalDate date;
    private String memberId;
    private String height;
    private String weight;
    private String muscle;
    private String fat;
    private String fatPercent;
    private String bmr;
    private String bmi;
    // builder를 통해서 반복된 getter setter 사용 방지 , @Query 어노테이션이랑 호환 안됨
    public Body toBodyEntity() {
        return Body.builder()
                .date(this.date)
                .height(this.height)
                .weight(this.weight)
                .muscle(this.muscle)
                .fat(this.fat)
                .fatPercent(this.fatPercent)
                .bmr(this.bmr)
                .bmi(this.bmi)
                .build();
    }
}
