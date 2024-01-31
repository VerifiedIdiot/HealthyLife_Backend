package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")


public class ElasticsearchCrudService {
    private final MedicineRepository medicineRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    public ElasticsearchCrudService(MedicineRepository medicineRepository, ElasticsearchOperations elasticsearchOperations, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.medicineRepository = medicineRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }
    // 단건 insert
    public void insert(ElasticsearchDto elasticsearchDto) {
        MedicineDocument document = elasticsearchDto.toDocument();
        medicineRepository.save(document);
    }
    // 외부데이터를 es서버에 전부 insert, 1000개씩 끊어서 insert
    @Transactional
    public void insertAll(List<MedicineDto> medicineDtos) {
        final int batchSize = 1000; // 배치 크기 설정
        List<MedicineDocument> batch = new ArrayList<>();

        for (MedicineDto dto : medicineDtos) {
            batch.add(dto.toDocument());

            // 배치 크기에 도달하면 데이터 저장 후 배치 초기화
            if (batch.size() == batchSize) {
                medicineRepository.saveAll(batch);
                batch.clear(); // 배치 초기화
            }
        }

        // 남은 데이터 처리
        if (!batch.isEmpty()) {
            medicineRepository.saveAll(batch);
        }
    }


    // put과 상호작용하는 서비스
    public Optional<MedicineDocument> update(String id, ElasticsearchDto elasticsearchDto) {
        if (medicineRepository.existsById(id)) {
            MedicineDocument updatedDocument = elasticsearchDto.toDocument();
            updatedDocument.setId(id); // 기존 ID 유지
            return Optional.of(medicineRepository.save(updatedDocument));
        }
        return Optional.empty(); // ID가 존재하지 않을 경우
    }
    // 건드리지마시오
    public void deleteAll() {
        medicineRepository.deleteAll();
    }
    // 모두 찾기 , 가급적 사용 x
    public List<ElasticsearchDto> findAll() {
        return StreamSupport.stream(medicineRepository.findAll().spliterator(), false)
                .map(MedicineDocument::toDto)
                .collect(Collectors.toList());
    }
    // 개별 지우기
    public boolean delete(String id) {
        if (medicineRepository.existsById(id)) {
            medicineRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    public List<ElasticsearchDto> findBySize(int page, int size) {
        int pageNum = (page - 1) * size;
        PageRequest pageRequest = PageRequest.of(pageNum, size);
        Page<MedicineDocument> resultPage = medicineRepository.findAll(pageRequest);

        // 결과를 ElasticsearchDto 리스트로 변환
        return resultPage.stream()
                .map(MedicineDocument::toDto)
                .collect(Collectors.toList());
    }
}
