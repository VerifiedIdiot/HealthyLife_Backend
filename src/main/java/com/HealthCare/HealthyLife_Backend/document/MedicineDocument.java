package com.HealthCare.HealthyLife_Backend.document;


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
    private Long id;

    @Field(type = FieldType.Text)
    private String product_name;

    @Field(type = FieldType.Text,analyzer = "nori_analyzer")
    private String functionalities;

    @Field(type = FieldType.Text)
    private String company;

}
