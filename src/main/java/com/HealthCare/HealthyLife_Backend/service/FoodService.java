package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public List<FoodDto> getFoodList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodPage = foodRepository.findAll(pageable);

        List<Food> foods = foodPage.getContent();
        List<FoodDto> foodDtos = new ArrayList<>();
        for (Food food : foods) {
            foodDtos.add(FoodDto.builder()
                    .name(food.getName())
                    .brand(food.getBrand())
                    // 다른 속성들도 추가해야 함
                    .build());
        }
        return foodDtos;
    }


    public int getFoodPage(Pageable pageable) {
        Page<Food> foodPage = foodRepository.findAll(pageable);
        return foodPage.getTotalPages();
    }

    public List<FoodDto> getFoodSortedByKeyword(String keyword) {
        return foodRepository.findByKeyword(keyword);
    }


    private static FoodDto fromExcelRow(Row row) {
        FoodDto foodDto = new FoodDto();

        foodDto.setName(getStringValue(row.getCell(0)));
        foodDto.setBrand(getStringValue(row.getCell(1)));
        foodDto.setClass1(getStringValue(row.getCell(2)));
        foodDto.setClass2(getStringValue(row.getCell(3)));
        foodDto.setServingSize(getStringValue(row.getCell(4)));
        foodDto.setServingUnit(getStringValue(row.getCell(5)));
        foodDto.setKcal(getStringValue(row.getCell(6)));
        foodDto.setProtein(getStringValue(row.getCell(7)));
        foodDto.setProvince(getStringValue(row.getCell(8)));
        foodDto.setCarbohydrate(getStringValue(row.getCell(9)));
        foodDto.setSugar(getStringValue(row.getCell(10)));
        foodDto.setDietaryFiber(getStringValue(row.getCell(11)));
        foodDto.setCalcium(getStringValue(row.getCell(12)));
        foodDto.setIron(getStringValue(row.getCell(13)));
        foodDto.setSalt(getStringValue(row.getCell(14)));
        foodDto.setZinc(getStringValue(row.getCell(15)));
        foodDto.setVitaB1(getStringValue(row.getCell(16)));
        foodDto.setVitaB2(getStringValue(row.getCell(17)));
        foodDto.setVitaB12(getStringValue(row.getCell(18)));
        foodDto.setVitaC(getStringValue(row.getCell(19)));
        foodDto.setCholesterol(getStringValue(row.getCell(20)));
        foodDto.setSaturatedFat(getStringValue(row.getCell(21)));
        foodDto.setTransFat(getStringValue(row.getCell(22)));
        foodDto.setIssuer(getStringValue(row.getCell(23)));

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


