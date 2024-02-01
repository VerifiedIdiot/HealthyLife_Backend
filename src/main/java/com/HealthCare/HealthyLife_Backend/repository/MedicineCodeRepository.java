package com.HealthCare.HealthyLife_Backend.repository;


import com.HealthCare.HealthyLife_Backend.entity.MedicineCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineCodeRepository extends JpaRepository<MedicineCode,Long> {
    List<MedicineCode> findByType(String type);

}
