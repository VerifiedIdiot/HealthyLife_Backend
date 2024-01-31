package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FoodDto {
    private String image;
    private String name;
    private String brand;
    private String class1;
    private String class2;
    private String servingSize;
    private String servingUnit;
    private String kcal;
    private String protein;
    private String province;
    private String carbohydrate;
    private String sugar;
    private String dietaryFiber;
    private String calcium;
    private String iron;
    private String salt;
    private String zinc;
    private String vitaB1;
    private String vitaB2;
    private String vitaB12;
    private String vitaC;
    private String cholesterol;
    private String saturatedFat;
    private String transFat;
    private String issuer;

    // builder를 통해서 반복된 getter setter 사용 방지 , @Query 어노테이션이랑 호환 안됨
    public Food toFoodEntity() {
        return Food.builder()

                .name(this.name)
                .brand(this.brand)
                .class1(this.class1)
                .class2(this.class2)
                .servingSize(this.servingSize)
                .servingUnit(this.servingUnit)
                .kcal(this.kcal)
                .protein(this.protein)
                .province(this.province)
                .carbohydrate(this.carbohydrate)
                .sugar(this.sugar)
                .dietaryFiber(this.dietaryFiber)
                .calcium(this.calcium)
                .iron(this.iron)
                .salt(this.salt)
                .zinc(this.zinc)
                .vitaB1(this.vitaB1)
                .vitaB2(this.vitaB2)
                .vitaB12(this.vitaB12)
                .vitaC(this.vitaC)
                .cholesterol(this.cholesterol)
                .saturatedFat(this.saturatedFat)
                .transFat(this.transFat)
                .issuer(this.issuer)
                .build();
        }
}
