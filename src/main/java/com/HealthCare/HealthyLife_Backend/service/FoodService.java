package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Value("${excel.file.path}")
    private String excelPath;

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

        foodDto.setNum((long) Double.parseDouble(getStringValue(row.getCell(0))));
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

    @Transactional
    public void saveFoodData() {
        try {
            List<FoodDto> foodDtoList = FoodService.readFromExcel(excelPath);
            List<Food> foodEntities = new ArrayList<>();

            for (FoodDto foodDto : foodDtoList) {
                foodEntities.add(foodDto.toFoodEntity());
            }

            foodRepository.saveAll(foodEntities);
        } catch (IOException e) {
            // 예외 처리 로직을 여기에 추가
            e.printStackTrace();
            // 혹은 로그에 기록하거나, 사용자에게 메시지를 전달하는 등의 처리를 수행할 수 있습니다.
        }
    }
}


