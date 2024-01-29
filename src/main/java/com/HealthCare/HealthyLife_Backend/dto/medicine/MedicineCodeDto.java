package com.HealthCare.HealthyLife_Backend.dto.medicine;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MedicineCodeDto {

    private String type;

    private String id;

    private String functionality;

    private List<String> materials;



}
