package com.HealthCare.HealthyLife_Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString
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


}