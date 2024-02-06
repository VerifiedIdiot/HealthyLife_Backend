package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchFilterService {
    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchFilterService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<ElasticsearchDto> findBySize(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        var searchHits = elasticsearchOperations.search(new NativeSearchQueryBuilder().withPageable(pageRequest).build(), MedicineDocument.class);

        return searchHits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().toDto())
                .collect(Collectors.toList());
    }
    public List<ElasticsearchDto> findByFilter(
            String query, String filter,
            String functionalities, String originType,
            String sortField, boolean sortAscending,
            int page, int size) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // "통합" 검색 모드일 때
        if (query != null && !query.isEmpty()) {
            if ("통합".equals(filter)) {
                // MultiMatchQueryBuilder를 사용하여 여러 필드에 대한 검색 수행
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(query, "product_name", "company", "report_no", "functionalities")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
                boolQueryBuilder.must(multiMatchQueryBuilder);
            } else {
                // 특정 필드에 대한 검색
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(filter.trim().replace("\"", ""), query.trim().replace("\"", "")));
            }
        } else {
            // 쿼리가 null 또는 비어있는 경우, 모든 문서를 매치
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }



        if (functionalities != null && !functionalities.isEmpty()) {
            BoolQueryBuilder functionalitiesQueryBuilder = QueryBuilders.boolQuery();

            // 쉼표로 구분된 값을 분리하고 쌍따옴표를 제거
            String[] functionalitiesArray = functionalities.split(",");
            for (String functionality : functionalitiesArray) {
                String sanitizedFunctionality = functionality.trim().replace("\"", "");
                functionalitiesQueryBuilder.should(QueryBuilders.matchQuery("functionalities", sanitizedFunctionality));
            }
            boolQueryBuilder.must(functionalitiesQueryBuilder);
        }

        if (originType != null && !originType.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("origin_type", originType.trim().replace("\"", "")));
            System.out.println(originType);
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        if (sortField != null && !sortField.isEmpty()) {
            queryBuilder.withSorts(SortBuilders.fieldSort(sortField).order(sortAscending ? SortOrder.ASC : SortOrder.DESC));
        }

        queryBuilder.withPageable(PageRequest.of(page - 1, size));

        NativeSearchQuery searchQuery = queryBuilder.build();
        var searchHits = elasticsearchOperations.search(searchQuery, MedicineDocument.class);

        return searchHits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().toDto())
                .collect(Collectors.toList());
    }


}
