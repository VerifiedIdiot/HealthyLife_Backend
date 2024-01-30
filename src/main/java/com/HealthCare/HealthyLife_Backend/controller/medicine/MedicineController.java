package com.HealthCare.HealthyLife_Backend.controller.medicine;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;


import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import com.HealthCare.HealthyLife_Backend.service.medicine.MedicineService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@RestController
@RequestMapping("/api/medicines")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class MedicineController implements CrudControllerInterface<MedicineDto, Long> {

    private final MedicineService medicineService;

    private final MedicineRepository medicineRepository;


    public MedicineController(MedicineService medicineService, MedicineRepository medicineRepository) {
        this.medicineService = medicineService;
        this.medicineRepository = medicineRepository;
    }


    @Override
    @GetMapping("/getCodes")
    public ResponseEntity<?> insert() {
        try {
            Map<String, String> funtionalities = medicineService.getCodes();
//            List<MedicineDto> medicineList = medicineService.getMedicineList();
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