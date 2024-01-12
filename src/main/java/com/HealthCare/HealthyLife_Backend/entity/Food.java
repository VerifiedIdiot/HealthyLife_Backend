package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "food") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long num;

    @Column(name = "food_cd")
    private String foodCd;

    @Column(name = "sampling_region_name")
    private String samplingRegionName;

    @Column(name = "sampling_month_name")
    private String samplingMonthName;

    @Column(name = "sampling_region_cd")
    private String samplingRegionCd;

    @Column(name = "sampling_month_cd")
    private String samplingMonthCd;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "desc_kor")
    private String descKor;

    @Column(name = "research_year")
    private String researchYear;

    @Column(name = "maker_name")
    private String makerName;

    @Column(name = "sub_ref_name")
    private String subRefName;

    @Column(name = "serving_size")
    private String servingSize;

    @Column(name = "serving_unit")
    private String servingUnit;

    @Column(name = "nutr_cont_1")
    private int nutrCont1;

    @Column(name = "nutr_cont_2")
    private int nutrCont2;

    @Column(name = "nutr_cont_3")
    private int nutrCont3;

    @Column(name = "nutr_cont_4")
    private int nutrCont4;

    @Column(name = "nutr_cont_5")
    private int nutrCont5;

    @Column(name = "nutr_cont_6")
    private int nutrCont6;

    @Column(name = "nutr_cont_7")
    private int nutrCont7;

    @Column(name = "nutr_cont_8")
    private int nutrCont8;

    @Column(name = "nutr_cont_9")
    private int nutrCont9;

}
