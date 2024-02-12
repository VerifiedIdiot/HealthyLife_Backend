package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findAll(Pageable pageable);
    @Query("SELECT DISTINCT f FROM Food f WHERE " +
            "(:name IS NULL OR f.name LIKE %:name%) AND " +
            "(:class1 IS NULL OR f.class1 LIKE %:class1%) AND " +
            "(:class2 IS NULL OR f.class2 LIKE %:class2%)")
    Page<Food> findByConditions(@Param("name") String name,
                                @Param("class1") String class1,
                                @Param("class2") String class2,
                                Pageable pageable);

//    FoodDto findByName(String keyword);

    // 문지예 캘린더 측 검색을 위해 추가 24/02/12
    @JsonView(Views.Internal.class)
    @Query("SELECT DISTINCT new com.HealthCare.HealthyLife_Backend.dto.FoodDto(" +
            "f.name, f.image, f.servingSize, f.kcal, f.protein, f.fat, f.carbohydrate, " +
            "f.sugar, f.dietaryFiber, f.calcium, f.vitaB1, f.vitaB2, f.vitaC, " +
            "f.cholesterol, f.saturatedFat, f.transFat) " +
            "FROM Food f WHERE f.name LIKE %:keyword%")
    List<FoodDto> findAllByName(@Param("keyword") String keyword);


    Optional<Food> findByName(String name);
}