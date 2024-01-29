package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import lombok.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchDto {

    private String type;


    private Long reportNo;


    private String name;


    private String company;

    private String functionalities;

    private String materials;


    public MedicineDocument toDocument(ElasticsearchDto elasticsearchDto) {
        return MedicineDocument.builder()
                .type(this.getType())
                .reportNo(this.getReportNo())
                .productName(this.getName())
                .functionalities(this.getFunctionalities())
                .materials(this.getMaterials())
                .company(this.getCompany())
                .build();
    }
}
