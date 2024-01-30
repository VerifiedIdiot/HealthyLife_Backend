package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import com.HealthCare.HealthyLife_Backend.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;
    private final MemberService memberService;
    private final FoodService foodService;
    private final FoodRepository foodRepository;

    public FoodDto getFoodByName(String brand) {
        List<Food> foods = foodRepository.findByBrand(brand); // findByBrand 같은 메서드를 사용해야 할 수도 있습니다.
        List<FoodDto> foodDtos = new ArrayList<>();

        for (Food food : foods) {
            FoodDto dto = convertToDto(food); // Food 객체를 FoodDto 객체로 변환
            foodDtos.add(dto);
        }

        // 여기서는 예시로 첫 번째 FoodDto를 반환하고 있습니다. 실제 로직은 요구사항에 따라 달라질 수 있습니다.
        return foodDtos.isEmpty() ? null : foodDtos.get(0);
    }

        private FoodDto convertToDto (Food food){
            if (food == null) {
                return null;
            }

            return FoodDto.builder()
                    .num(food.getNum())
                    .image(food.getImage())
                    .name(food.getName())
                    .brand(food.getBrand())
                    .class1(food.getClass1())
                    .class2(food.getClass2())
                    .servingSize(food.getServingSize())
                    .servingUnit(food.getServingUnit())
                    .kcal(food.getKcal())
                    .protein(food.getProtein())
                    .province(food.getProvince())
                    .carbohydrate(food.getCarbohydrate())
                    .sugar(food.getSugar())
                    .dietaryFiber(food.getDietaryFiber())
                    .calcium(food.getCalcium())
                    .iron(food.getIron())
                    .salt(food.getSalt())
                    .zinc(food.getZinc())
                    .vitaB1(food.getVitaB1())
                    .vitaB2(food.getVitaB2())
                    .vitaB12(food.getVitaB12())
                    .vitaC(food.getVitaC())
                    .cholesterol(food.getCholesterol())
                    .saturatedFat(food.getSaturatedFat())
                    .transFat(food.getTransFat())
                    .issuer(food.getIssuer())
                    .build();
        }

}
