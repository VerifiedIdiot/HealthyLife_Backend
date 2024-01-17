package com.HealthCare.HealthyLife_Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("company")
    private String company;

    @JsonProperty("functionalities")
    private String functionalities;

    @Getter
    public static class FunctionalityCodes {

        @JsonProperty("id")
        private String id;
        @JsonProperty("functionality")
        private String functionality;


    }
}
