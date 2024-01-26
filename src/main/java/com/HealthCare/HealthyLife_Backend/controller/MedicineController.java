package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.MedicineDto;


import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import com.HealthCare.HealthyLife_Backend.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController implements CrudControllerInterface<MedicineDto, Long> {

    private final MedicineService medicineService;

    private final MedicineRepository medicineRepository;


    public MedicineController(MedicineService medicineService, MedicineRepository medicineRepository) {
        this.medicineService = medicineService;

        this.medicineRepository = medicineRepository;
    }


    @Override
    public ResponseEntity<?> insert() {
        try {
//            Map<String, String> funtionalities = medicineService.getFunctionalities();
            List<MedicineDto> medicineList = medicineService.getMedicineList();
            return ResponseEntity.ok("건강기능식품 insert");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }



    @Override
    public ResponseEntity<List<MedicineDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<MedicineDto> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<MedicineDto> update(Long id, MedicineDto medicineDto) {
        return null;
    }



}