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
    private final ElasticsearchOperations elasticsearchOperations;

    // MedicineRepository와 ElasticsearchOperations에 대한 생성자 주입
    public ElasticsearchFilterService(MedicineRepository medicineRepository, ElasticsearchOperations elasticsearchOperations) {
        this.medicineRepository = medicineRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    // 드롭다운 필터를 사용하여 검색하고 결과를 정렬하는 메소드
    public List<ElasticsearchDto> searchWithDropdown(String productName, Long reportNo, String company, String sortField, boolean sortAscending) {
        // 여러 필터를 조합하기 위한 불리언 쿼리 빌더 초기화
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 제품 이름에 대한 필터가 null이 아닌 경우 추가
        if (productName != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("productName", productName));
        }
        // 보고서 번호에 대한 필터가 null이 아닌 경우 추가
        if (reportNo != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("reportNo", reportNo));
        }
        // 회사에 대한 필터가 null이 아닌 경우 추가
        if (company != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("company", company));
        }

        // NativeSearchQueryBuilder를 사용하여 쿼리를 구성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        // 정렬 필드가 제공된 경우 정렬 조건 추가
        if (sortField != null && !sortField.isEmpty()) {
            queryBuilder.withSorts(SortBuilders.fieldSort(sortField).order(sortAscending ? SortOrder.ASC : SortOrder.DESC));
        }

        // 검색 쿼리를 구성
        NativeSearchQuery searchQuery = queryBuilder.build();
        // 검색을 실행하고 결과를 받음
        var searchHits = elasticsearchOperations.search(searchQuery, MedicineDocument.class);

        // 검색 결과를 ElasticsearchDto 객체 리스트로 변환
        return searchHits.getSearchHits()
                .stream() // 스트림 생성: 검색 결과를 순차적으로 처리하기 위한 스트림을 생성
                .map(hit -> hit.getContent().toDto()) // 매핑: 각 검색 결과의 실제 내용을 가져와 DTO로 변환
                .collect(Collectors.toList()); // 수집: 변환된 DTO 객체들을 리스트로 수집하여 반환
    }
}
