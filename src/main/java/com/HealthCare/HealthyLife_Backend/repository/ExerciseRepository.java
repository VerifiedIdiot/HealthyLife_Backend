package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.dto.FoodDto;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Page<Exercise> findAll(Pageable pageable);
    Page<Exercise> findByNameContaining(String name, Pageable pageable);

    Page<Exercise> findByMuscleAndDifficultyContaining(String muscle, String difficulty, Pageable pageable);

    @Query("SELECT f FROM Exercise f WHERE f.name LIKE %:name% AND f.muscle = :muscle")
    Page<Exercise> findByNameAndMuscleContaining(@Param("name") String name, @Param("muscle") String muscle, Pageable pageable);

    @Query("SELECT f FROM Exercise f WHERE f.name LIKE %:name% AND f.difficulty = :difficulty")
    Page<Exercise> findByNameAndDifficultyContaining(@Param("name") String name, @Param("difficulty") String difficulty, Pageable pageable);

    Page<Exercise> findByDifficultyContaining(String difficulty, Pageable pageable);

    Page<Exercise> findByMuscleContaining(String muscle, Pageable pageable);

    @Query("SELECT f FROM Exercise f WHERE f.name LIKE %:name% AND f.muscle = :muscle AND f.difficulty = :difficulty")
    Page<Exercise> findByNameAndMuscleAndDifficultyContaining(@Param("name") String name, @Param("muscle") String muscle, @Param("difficulty") String difficulty, Pageable pageable);


    ExerciseDto findByName(String keyword);

}