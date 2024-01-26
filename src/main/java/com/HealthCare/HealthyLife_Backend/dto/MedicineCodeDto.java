package com.HealthCare.HealthyLife_Backend.dto;
import com.HealthCare.HealthyLife_Backend.entity.Medicine;
import com.HealthCare.HealthyLife_Backend.entity.MedicineCode;
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

public class MedicineCodeDto {
    @JsonProperty("type")
    private String medicineType;
    @JsonProperty("id")
    private String medicineCode;
    @JsonProperty("functionality")
    private String functionality;
    @JsonProperty("materials")
    private List<String> materials;

    public MedicineCode toEntity() {
        return MedicineCode.builder()
                .medicineType(this.getMedicineType())
                .medicineCode(this.getMedicineCode())
                .functionality(this.getFunctionality())
                .materials(this.getMaterials())
                .build();
    }

}
