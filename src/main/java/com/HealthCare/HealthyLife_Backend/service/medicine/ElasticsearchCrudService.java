package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchCrudService {
    private final MedicineRepository medicineRepository;
    private final RestTemplate restTemplate;

    public ElasticsearchCrudService(MedicineRepository medicineRepository, RestTemplate restTemplate) {
        this.medicineRepository = medicineRepository;
        this.restTemplate = restTemplate;
    }

    public void insert(ElasticsearchDto elasticsearchDto) {
        MedicineDocument document = elasticsearchDto.toDocument();
        medicineRepository.save(document);
    }

    public Optional<MedicineDocument> update(String id, ElasticsearchDto elasticsearchDto) {
        if (medicineRepository.existsById(id)) {
            MedicineDocument updatedDocument = elasticsearchDto.toDocument();
            updatedDocument.setId(id); // 기존 ID 유지
            return Optional.of(medicineRepository.save(updatedDocument));
        }
        return Optional.empty(); // ID가 존재하지 않을 경우
    }

    public void deleteAll() {
        medicineRepository.deleteAll();
    }

    public List<ElasticsearchDto> findAll() {
        return StreamSupport.stream(medicineRepository.findAll().spliterator(), false)
                .map(MedicineDocument::toDto)
                .collect(Collectors.toList());
    }

    public boolean delete(String id) {
        if (medicineRepository.existsById(id)) {
            medicineRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
