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
    Page<Food> findByNameContaining(String name, Pageable pageable);

    Page<Food> findByClass1ContainingAndClass2Containing(String class1, String class2, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.name LIKE %:name% AND f.class1 = :class1")
    Page<Food> findByNameAndClass1Containing(@Param("name") String name, @Param("class1") String class1, Pageable pageable);


    Page<Food> findByClass1Containing(String class1, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.name LIKE %:name% AND f.class1 = :class1 AND f.class2 = :class2")
    Page<Food> findByNameAndClass1AndClass2Containing(@Param("name") String name, @Param("class1") String class1, @Param("class2") String class2, Pageable pageable);


    FoodDto findByName(String keyword);
}