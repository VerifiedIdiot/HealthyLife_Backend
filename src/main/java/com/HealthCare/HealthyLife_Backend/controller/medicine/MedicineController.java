package com.HealthCare.HealthyLife_Backend.controller.medicine;


import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineCodeDto;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;


import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import com.HealthCare.HealthyLife_Backend.service.medicine.ElasticsearchCrudService;
import com.HealthCare.HealthyLife_Backend.service.medicine.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@Slf4j
@RestController
@RequestMapping("/api/medicines")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class MedicineController {

    private final MedicineService medicineService;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchCrudService elasticsearchCrudService;
    private final MedicineRepository medicineRepository;


    public MedicineController(MedicineService medicineService, ElasticsearchOperations elasticsearchOperations, ElasticsearchCrudService elasticsearchCrudService, MedicineRepository medicineRepository) {
        this.medicineService = medicineService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchCrudService = elasticsearchCrudService;
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
    public ResponseEntity<?> insertMedicineList() {
        try {
            List<MedicineDto> allMedicineDtos = medicineService.getMedicineList();

            if (allMedicineDtos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            long startTime = System.currentTimeMillis(); // 시작 시간 기록

            elasticsearchCrudService.insertAll(allMedicineDtos);

            long endTime = System.currentTimeMillis(); // 종료 시간 기록
            long duration = endTime - startTime; // 걸린 시간 계산

            return ResponseEntity.ok("Insert 완료. 총 걸린 시간: " + duration + "ms, 총 삽입된 데이터 수: " + allMedicineDtos.size());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 내부 오류 발생");
        }
    }
}