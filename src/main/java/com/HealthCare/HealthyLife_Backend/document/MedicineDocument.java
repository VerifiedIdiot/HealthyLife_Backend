package com.HealthCare.HealthyLife_Backend.document;


import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;


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

    @Field(name = "type", type = FieldType.Keyword)
    private String type;

    @Field(name = "report_no", type = FieldType.Long)
    private Long reportNo;

    @Field(name = "product_name", type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private List<String> functionalities;



    @Field(name = "company",type = FieldType.Text)
    private String company;


    public ElasticsearchDto toDto() {
        return ElasticsearchDto.builder()
                .id(this.getId())
                .type(this.getType())
                .reportNo(this.getReportNo())
                .name(this.getProductName())
                .functionalities(this.getFunctionalities())
                .company(this.getCompany())
                .build();
    }

}
