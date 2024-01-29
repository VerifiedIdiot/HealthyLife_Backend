package com.HealthCare.HealthyLife_Backend.document;


import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;


@Document(indexName = "medicine_index")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter @Setter
//@Setting(settingPath = "elastic/es-setting.json")
//@Mapping(mappingPath = "elastic/es-mapping.json")
public class MedicineDocument {
    @Id
    @Field(name = "medicine_id", type = FieldType.Keyword)
    private String id;

    @Field(name = "type", type = FieldType.Text)
    private String type;

    @Field(name = "report_no", type = FieldType.Long)
    private Long reportNo;

    @Field(name = "product_name", type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String functionalities;

    // 키워드 타입으로 분석되는 필드 (동일한 데이터를 다른 필드에 저장)
    @Field(type = FieldType.Keyword)
    private String functionalitiesKeyword;


    @Field(name = "materials", type = FieldType.Text, analyzer = "nori_analyzer")
    private String materials;

    @Field(name = "company",type = FieldType.Text)
    private String company;


    public ElasticsearchDto toDto() {
        return ElasticsearchDto.builder()
                .id(this.getId())
                .type(this.getType())
                .reportNo(this.getReportNo())
                .name(this.getProductName())
                .functionalities(this.getFunctionalities())
                .materials(this.getMaterials())
                .company(this.getCompany())
                .build();
    }

}
