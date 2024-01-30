package com.HealthCare.HealthyLife_Backend.dto.medicine;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import lombok.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchDto {
    private String id;

    private String type;


    private Long reportNo;


    private String name;


    private String company;

    private String functionalities;

    private String materials;


    public MedicineDocument toDocument() {
        return MedicineDocument.builder()
                .id(this.getId())
                .type(this.getType())
                .reportNo(this.getReportNo())
                .productName(this.getName())
                .functionalities(this.getFunctionalities())
                .materials(this.getMaterials())
                .company(this.getCompany())
                .build();
    }


}