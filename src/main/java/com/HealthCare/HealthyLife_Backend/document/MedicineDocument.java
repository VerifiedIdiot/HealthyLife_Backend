package com.HealthCare.HealthyLife_Backend.document;


import com.HealthCare.HealthyLife_Backend.dto.ElasticsearchDto;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;


@Document(indexName = "medicine_index")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
//@Setting(settingPath = "elastic/es-setting.json")
//@Mapping(mappingPath = "elastic/es-mapping.json")
public class MedicineDocument {
    @Id
    @Field(name = "medicine_id")
    private Long id;

    @Field(name = "type", type = FieldType.Text)
    private String type;

    @Field(name = "report_no", type = FieldType.Long)
    private Long reportNo;

    @Field(name = "product_name", type = FieldType.Text)
    private String productName;

    @Field(name = "functionalities", type = FieldType.Text, analyzer = "nori_analyzer")
    private String functionalities;

    @Field(name = "materials", type = FieldType.Text, analyzer = "nori_analyzer")
    private String materials;

    @Field(name = "company",type = FieldType.Text)
    private String company;


    public ElasticsearchDto toDto() {
        return ElasticsearchDto.builder()
                .type(this.getType())
                .reportNo(this.getReportNo())
                .name(this.getProductName())
                .functionalities(this.getFunctionalities())
                .materials(this.getMaterials())
                .company(this.getCompany())
                .build();
    }

}
