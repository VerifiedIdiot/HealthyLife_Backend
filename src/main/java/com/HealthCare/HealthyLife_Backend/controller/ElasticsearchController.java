package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.Interface.CrudControllerInterface;
import com.HealthCare.HealthyLife_Backend.dto.ElasticsearchDto;

import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import com.HealthCare.HealthyLife_Backend.service.ElasticsearchService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elastic-search")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchController implements CrudControllerInterface<ElasticsearchDto, Long> {
    private final MedicineRepository medicineRepository;

    private final ElasticsearchService elasticsearchService;

    public ElasticsearchController(MedicineRepository medicineRepository, ElasticsearchService elasticsearchService) {
        this.medicineRepository = medicineRepository;
        this.elasticsearchService = elasticsearchService;
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertDataTest(@RequestBody ElasticsearchDto elasticsearchDto) {
        try {
            elasticsearchService.insert(elasticsearchDto);
            return ResponseEntity.ok("건강기능식품 insert");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDataTest(@PathVariable Long id) {
        try {
            boolean result = elasticsearchService.delete(id);
            if (result) {
                return ResponseEntity.ok("건강기능식품이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Delete 실패: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<ElasticsearchDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ElasticsearchDto> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> insert() {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ElasticsearchDto> update(Long id, ElasticsearchDto elasticsearchDto) {
        return null;
    }
}
