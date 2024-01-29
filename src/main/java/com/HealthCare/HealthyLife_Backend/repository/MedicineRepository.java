package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true")
public interface MedicineRepository extends ElasticsearchRepository<MedicineDocument,Long> {
}
