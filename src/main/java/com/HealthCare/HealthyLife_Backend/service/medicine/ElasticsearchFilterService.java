package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
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
    public List<ElasticsearchDto> searchWithDropdown(
            String productName, Long reportNo, String company,
            String functionalities, String type,
            String sortField, boolean sortAscending,
            int page, int size) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (productName != null) {

            boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("product_name", productName));
        }

        if (reportNo != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("report_no", reportNo));

        }

        if (company != null) {

            boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("company", company));
            boolQueryBuilder.must(QueryBuilders.termQuery("company", "(주)"));
        }

        if (functionalities != null) {

            boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("functionalities", functionalities));
        }

        if (type != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("type", type));
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
