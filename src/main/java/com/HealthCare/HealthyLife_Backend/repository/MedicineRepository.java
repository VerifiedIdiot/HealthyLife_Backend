package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true")
public interface MedicineRepository extends ElasticsearchRepository<MedicineDocument,Long> {
}
