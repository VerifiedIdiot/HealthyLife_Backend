package com.HealthCare.HealthyLife_Backend.dto.medicine;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicineDto {


    private String type;


    private String reportNo;


    private String name;


    private String company;


    private List<String> functionalities;


    public MedicineDocument toDocument() {
        return MedicineDocument.builder()
                .type(this.getType())
                .reportNo(this.getReportNo())
                .productName(this.getName())
                .company(this.getCompany())
                .functionalities(this.getFunctionalities())
                .build();
    }

}