package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findAll(Pageable pageable);
    @Query("SELECT new com.HealthCare.HealthyLife_Backend.dto.FoodDto(f.image, f.name, f.brand, f.class1, f.class2, " +
            "f.servingSize, f.servingUnit, f.kcal, f.protein, f.province, f.carbohydrate, f.sugar, f.dietaryFiber, " +
            "f.calcium, f.iron, f.salt, f.zinc, f.vitaB1, f.vitaB2, f.vitaB12, f.vitaC, f.cholesterol, " +
            "f.saturatedFat, f.transFat, f.issuer) " +
            "FROM Food f WHERE f.name LIKE %:keyword% OR f.brand LIKE %:keyword% ORDER BY f.name ASC")
    List<FoodDto> findByKeyword(@Param("keyword") String keyword);

}