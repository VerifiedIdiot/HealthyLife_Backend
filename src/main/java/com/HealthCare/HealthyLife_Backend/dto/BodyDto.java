package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.entity.Member;
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
    private String memberEmail;
    private String height;
    private String weight;
    private String muscle;
    private String fat;
    private String fatPercent;
    private String bmr;
    private String bmi;
}
