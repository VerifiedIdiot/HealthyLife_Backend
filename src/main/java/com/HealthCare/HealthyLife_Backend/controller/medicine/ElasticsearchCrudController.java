package com.HealthCare.HealthyLife_Backend.controller.medicine;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;

import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import com.HealthCare.HealthyLife_Backend.service.medicine.ElasticsearchCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/elastic-search")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchCrudController {
    private final MedicineRepository medicineRepository;

    private final ElasticsearchCrudService elasticsearchCrudService;

    public ElasticsearchCrudController(MedicineRepository medicineRepository, ElasticsearchCrudService elasticsearchCrudService) {
        this.medicineRepository = medicineRepository;
        this.elasticsearchCrudService = elasticsearchCrudService;
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertDataTest(@RequestBody ElasticsearchDto elasticsearchDto) {
        try {
            elasticsearchCrudService.insert(elasticsearchDto);
            return ResponseEntity.ok("건강기능식품 insert");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }



    @GetMapping("/findAll")
    public ResponseEntity<?> findAllData() {
        try {
            List<ElasticsearchDto> results = elasticsearchCrudService.findAll();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 조회 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDataTest(@PathVariable String id) {
        boolean result = elasticsearchCrudService.delete(id);
        if (result) {
            return ResponseEntity.ok(id + "번 건강기능식품이 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateData(@PathVariable String id, @RequestBody ElasticsearchDto elasticsearchDto) {
        Optional<MedicineDocument> updatedDocument = elasticsearchCrudService.update(id, elasticsearchDto);
        if (updatedDocument.isPresent()) {
            return ResponseEntity.ok(id + "번 업데이트 성공");
        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디");
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllTest() {
        try {
            elasticsearchCrudService.deleteAll();
            return ResponseEntity.ok("모든 건강기능식품이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("전체 삭제 실패: " + e.getMessage());
        }
    }

}
