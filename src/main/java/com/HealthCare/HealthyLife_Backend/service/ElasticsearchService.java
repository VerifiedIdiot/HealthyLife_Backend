package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.dto.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchService {
    private final MedicineRepository medicineRepository;
    private final RestTemplate restTemplate;

    public ElasticsearchService(MedicineRepository medicineRepository, RestTemplate restTemplate) {
        this.medicineRepository = medicineRepository;
        this.restTemplate = restTemplate;
    }

    public MedicineDocument insert(ElasticsearchDto elasticsearchDto) {
        MedicineDocument document = elasticsearchDto.toDocument(elasticsearchDto);
        return medicineRepository.save(document);
    }

    public boolean delete(Long id) {
        if (medicineRepository.existsById(id)) {
            medicineRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
