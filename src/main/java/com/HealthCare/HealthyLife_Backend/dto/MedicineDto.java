package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Medicine;
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
public class MedicineDto {


    private String type;


    private String reportNo;


    private String name;


    private String company;


    private List<String> functionalities;


    private List<String> materials;


    @Getter
    public static class FunctionalityCodes {
        @JsonProperty("id")
        private String id;

        @JsonProperty("functionality")
        private String functionality;
    }

    public Medicine toEntity() {
        return Medicine.builder()
                .type(this.getType())
                .reportNo(this.getReportNo())
                .name(this.getName())
                .company(this.getCompany())
                .functionalities(this.getFunctionalities())
                .materials(this.getFunctionalities())
                .build();
    }
}