package com.HealthCare.HealthyLife_Backend.controller.medicine;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineCodeDto;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;


import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import com.HealthCare.HealthyLife_Backend.service.medicine.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@Slf4j
@RestController
@RequestMapping("/api/medicines")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class MedicineController  {

    private final MedicineService medicineService;

    private final MedicineRepository medicineRepository;


    public MedicineController(MedicineService medicineService, MedicineRepository medicineRepository) {
        this.medicineService = medicineService;
        this.medicineRepository = medicineRepository;
    }



    @GetMapping("/get-codes")
    public ResponseEntity<?> getCodes() {
        try {
            List<MedicineCodeDto> codes = medicineService.getCodes();
            if (codes.isEmpty()) {

                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(codes);
        } catch (Exception e) {
            // 서버 내부 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().body("서버 내부 오류 발생");
        }
    }

    @GetMapping("/get-medicines")
    public ResponseEntity<?> getMedicineList() {
        try {
            List<MedicineDto> allMedicineDtos = medicineService.getMedicineList();
            if (allMedicineDtos.isEmpty()) {

                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(allMedicineDtos);
        } catch (Exception e) {
            // 서버 내부 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().body("서버 내부 오류 발생");
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertMedicines() {
        try {
            List<MedicineCodeDto> codes = medicineService.getCodes();
            if (codes.isEmpty()) {

                return ResponseEntity.notFound().build();
            }
            List<MedicineDto> allMedicineDtos = medicineService.getMedicineList();
            if (allMedicineDtos.isEmpty()) {

                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(codes);
        } catch (Exception e) {
            // 서버 내부 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.internalServerError().body("서버 내부 오류 발생");
        }
    }



}