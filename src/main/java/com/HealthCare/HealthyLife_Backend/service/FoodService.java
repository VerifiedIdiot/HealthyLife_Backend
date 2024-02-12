package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

@Service
@Slf4j
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
            FoodDto foodDto = food.toFoodDto(); // Food 엔티티를 FoodDto로 변환
            foodDtos.add(foodDto);
        }
        return foodDtos;
    }

    public List<FoodDto> getFoodSortedByKeywordAndClass1AndClass2(String keyword, String class1, String class2, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foods;

        if (keyword != null || class1 != null || class2 != null) {
            foods = foodRepository.findByConditions(keyword, class1, class2, pageable);
        } else {
            // 모든 조건이 null인 경우 모든 음식 데이터를 반환
            foods = foodRepository.findAll(pageable);
        }

        List<FoodDto> foodDtos = new ArrayList<>();
        for (Food food : foods.getContent()) {
            foodDtos.add(food.toFoodDto());
        }
        return foodDtos;
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
        foodDto.setFat(getStringValue(row.getCell(8)));
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
    // 엑셀에서 중복값 발생할 경우 해당 레코드를 덮어 쓰는 로직을 추가했음
    @Transactional
    public void saveFoodData() {
        try {
            // 엑셀 파일로부터 읽어온 FoodDto 리스트를 불러옵니다.
            List<FoodDto> foodDtoList = FoodService.readFromExcel(excelPath);

            // 읽어온 FoodDto 리스트를 순회합니다.
            for (FoodDto foodDto : foodDtoList) {
                // 데이터베이스에서 동일한 'name'을 가진 Food 엔티티를 조회합니다.
                Optional<Food> existingFoodOpt = foodRepository.findByName(foodDto.getName());

                if (existingFoodOpt.isPresent()) {
                    // 만약 동일한 'name'을 가진 Food 엔티티가 데이터베이스에 존재한다면, 해당 엔티티를 업데이트합니다.
                    Food existingFood = existingFoodOpt.get();
                    updateFoodEntityFromDto(existingFood, foodDto); // FoodDto의 정보로 기존 Food 엔티티를 업데이트합니다.
                    foodRepository.save(existingFood); // 업데이트된 엔티티를 저장합니다. 이 과정에서 JPA의 변경 감지 기능(dirty checking)이 엔티티의 변경 사항을 자동으로 감지하고 업데이트 쿼리를 실행합니다.
                } else {
                    // 동일한 'name'을 가진 Food 엔티티가 데이터베이스에 존재하지 않는 경우, 새로운 Food 엔티티를 생성합니다.
                    Food newFood = new Food(); // FoodDto로부터 새 Food 엔티티를 생성합니다. 여기서는 기본 생성자를 사용한 후, 필드를 업데이트합니다.
                    updateFoodEntityFromDto(newFood, foodDto); // FoodDto의 정보로 새 Food 엔티티의 필드를 채웁니다.
                    foodRepository.save(newFood); // 새로운 엔티티를 데이터베이스에 저장합니다.
                }
            }
        } catch (IOException e) {
            // 파일 읽기 중 발생할 수 있는 IOException을 처리합니다.
            e.printStackTrace();
        }
    }


    // 엑셀에서 중복이름을 바꿔지는 메서드 , 빌더패턴을 사용할 수 없는이유는 , 빌더는 객체를 초기화 시켜서, 기존 중복된 dto에 대해서
    // 초기화 작업이 일어나기때문에 고전적인 게터 세터를 사용할 수 밖에 없다.
    private void updateFoodEntityFromDto(Food food, FoodDto foodDto) {
        food.setName(foodDto.getName());
        food.setImage(foodDto.getImage());
        food.setBrand(foodDto.getBrand());
        food.setClass1(foodDto.getClass1());
        food.setClass2(foodDto.getClass2());
        food.setServingSize(foodDto.getServingSize());
        food.setServingUnit(foodDto.getServingUnit());
        food.setKcal(foodDto.getKcal());
        food.setProtein(foodDto.getProtein());
        food.setFat(foodDto.getFat());
        food.setCarbohydrate(foodDto.getCarbohydrate());
        food.setSugar(foodDto.getSugar());
        food.setDietaryFiber(foodDto.getDietaryFiber());
        food.setCalcium(foodDto.getCalcium());
        food.setIron(foodDto.getIron());
        food.setSalt(foodDto.getSalt());
        food.setZinc(foodDto.getZinc());
        food.setVitaB1(foodDto.getVitaB1());
        food.setVitaB2(foodDto.getVitaB2());
        food.setVitaB12(foodDto.getVitaB12());
        food.setVitaC(foodDto.getVitaC());
        food.setCholesterol(foodDto.getCholesterol());
        food.setSaturatedFat(foodDto.getSaturatedFat());
        food.setTransFat(foodDto.getTransFat());
        food.setIssuer(foodDto.getIssuer());
    }
}


