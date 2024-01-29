package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchFilterService {
    private final MedicineRepository medicineRepository;

    private ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchFilterService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }
    public List<ElasticsearchDto> searchWithDropdown(String productName, Long reportNo, String company, String sortField, boolean sortAscending) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 조건에 따른 쿼리 구성
        if (productName != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("productName", productName));
        }
        if (reportNo != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("reportNo", reportNo));
        }
        if (company != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("company", company));
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        // 정렬 조건 추가
        if (sortField != null && !sortField.isEmpty()) {
            queryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortAscending ? SortOrder.ASC : SortOrder.DESC));
        }

        NativeSearchQuery searchQuery = queryBuilder.build();
        var searchHits = elasticsearchOperations.search(searchQuery, MedicineDocument.class);

        // 결과 변환
        return searchHits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().toDto())
                .collect(Collectors.toList());
    }
}
