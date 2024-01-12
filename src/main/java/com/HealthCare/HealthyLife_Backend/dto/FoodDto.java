package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FoodDto {
    private long num;
    private String foodCd;
    private String samplingRegionName;
    private String samplingMonthName;
    private String samplingRegionCd;
    private String samplingMonthCd;
    private String groupName;
    private String descKor;
    private String researchYear;
    private String makerName;
    private String subRefName;
    private String servingSize;
    private String servingUnit;
    private int nutrCont1;
    private int nutrCont2;
    private int nutrCont3;
    private int nutrCont4;
    private int nutrCont5;
    private int nutrCont6;
    private int nutrCont7;
    private int nutrCont8;
    private int nutrCont9;

    // 생성자, 게터, 세터 등 필요한 메서드들을 추가하시면 됩니다.
    public static List<FoodDto> readFromExcel(String filePath) throws IOException {
        List<FoodDto> foodDtoList = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(FoodDto.class.getClassLoader().getResourceAsStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming your data is in the first sheet

            // Skip the header row
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                FoodDto foodDto = fromExcelRow(row);
                foodDtoList.add(foodDto);
            }
        }

        return foodDtoList;
    }


    private static FoodDto fromExcelRow(Row row) {
        FoodDto foodDto = new FoodDto();

        foodDto.setNum((long) row.getCell(0).getNumericCellValue());
        foodDto.setFoodCd(getStringValue(row.getCell(1)));
        foodDto.setSamplingRegionName(getStringValue(row.getCell(2)));
        foodDto.setSamplingMonthName(getStringValue(row.getCell(3)));
        foodDto.setSamplingRegionCd(getStringValue(row.getCell(4)));
        foodDto.setSamplingMonthCd(getStringValue(row.getCell(5)));
        foodDto.setGroupName(getStringValue(row.getCell(6)));
        foodDto.setDescKor(getStringValue(row.getCell(7)));
        foodDto.setResearchYear(getStringValue(row.getCell(8)));
        foodDto.setMakerName(getStringValue(row.getCell(9)));
        foodDto.setSubRefName(getStringValue(row.getCell(10)));
        foodDto.setServingSize(getStringValue(row.getCell(11)));
        foodDto.setServingUnit(getStringValue(row.getCell(12)));
        foodDto.setNutrCont1((int) row.getCell(13).getNumericCellValue());
        foodDto.setNutrCont2((int) row.getCell(14).getNumericCellValue());
        foodDto.setNutrCont3((int) row.getCell(15).getNumericCellValue());
        foodDto.setNutrCont4((int) row.getCell(16).getNumericCellValue());
        foodDto.setNutrCont5((int) row.getCell(17).getNumericCellValue());
        foodDto.setNutrCont6((int) row.getCell(18).getNumericCellValue());
        foodDto.setNutrCont7((int) row.getCell(19).getNumericCellValue());
        foodDto.setNutrCont8((int) row.getCell(20).getNumericCellValue());
        foodDto.setNutrCont9((int) row.getCell(21).getNumericCellValue());

        return foodDto;
    }

    private static String getStringValue(Cell cell) {
        return cell == null ? null : cell.getStringCellValue();
    }


    // builder를 통해서 반복된 getter setter 사용 방지 , @Query 어노테이션이랑 호환 안됨
    public Food toFoodEntity() {
        return Food.builder()
                .num(this.num)
                .foodCd(this.foodCd)
                .samplingRegionName(this.samplingRegionName)
                .samplingMonthName(this.samplingMonthName)
                .samplingRegionCd(this.samplingRegionCd)
                .samplingMonthCd(this.samplingMonthCd)
                .groupName(this.groupName)
                .descKor(this.descKor)
                .researchYear(this.researchYear)
                .makerName(this.makerName)
                .subRefName(this.subRefName)
                .servingSize(this.servingSize)
                .servingUnit(this.servingUnit)
                .nutrCont1(this.nutrCont1)
                .nutrCont2(this.nutrCont2)
                .nutrCont3(this.nutrCont3)
                .nutrCont4(this.nutrCont4)
                .nutrCont5(this.nutrCont5)
                .nutrCont6(this.nutrCont6)
                .nutrCont7(this.nutrCont7)
                .nutrCont8(this.nutrCont8)
                .nutrCont9(this.nutrCont9)
                .build();
    }


}
