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
            String functionalities, String type,
            String sortField, boolean sortAscending,
            int page, int size) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // "통합" 검색 모드일 때
        if ("통합".equals(filter)) {
            BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
            shouldQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery("product_name", query));
            shouldQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery("company", query));
            shouldQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery("functionalities", query));
            boolQueryBuilder.must(shouldQueryBuilder);
        } else {
            // 특정 필드에 대한 검색이 필요한 경우
            boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery(filter, query));
        }



        if (functionalities != null && !functionalities.isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("functionalities", functionalities));
        }

        if (type != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("type", type));
            System.out.println(type);
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
