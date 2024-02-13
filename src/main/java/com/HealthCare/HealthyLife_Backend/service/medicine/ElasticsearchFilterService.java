package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.document.MedicineDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHits;
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
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchFilterService {
    private final ElasticsearchOperations elasticsearchOperations;

    public long getTotalCount() {
        return elasticsearchOperations.count(new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .build(), MedicineDocument.class);
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
                // "통합" 검색 시, 각 필드에 대해 명시적으로 우선순위를 지정합니다.
                BoolQueryBuilder combinedQuery = QueryBuilders.boolQuery();
                combinedQuery.should(QueryBuilders.matchQuery("functionalities", query).boost(4)); // 가장 높은 우선순위
                combinedQuery.should(QueryBuilders.matchQuery("product_name", query).boost(3));
                combinedQuery.should(QueryBuilders.matchQuery("company", query).boost(2));
                combinedQuery.should(QueryBuilders.matchQuery("report_no", query).boost(1)); // 가장 낮은 우선순위

                boolQueryBuilder.must(combinedQuery);
            } else {
                // 특정 필드에 대한 검색
                switch (filter) {
                    case "제품명":
                        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("product_name", query.trim().replace("\"", "")));
                        break;
                    case "제조사":
                        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("company", query.trim().replace("\"", "")));
                        break;
                    case "신고번호":
                        // 사용자 입력이 연도만 포함하는 경우 (예: 2012)
                        if (query.trim().length() == 4) {
                            // 연도를 기반으로 prefix query 사용
                            PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery("report_no", query.trim().replace("\"", ""));
                            boolQueryBuilder.must(prefixQuery);
                        } else {
                            // 전체 신고번호를 기반으로 정확한 매칭
                            WildcardQueryBuilder wildcardQuery = QueryBuilders.wildcardQuery("report_no", "*" + query.trim().replace("\"", "") + "*");
                            boolQueryBuilder.must(wildcardQuery);
                        }
                        break;
                    default:
                        // 제공된 필터가 위의 세 가지 경우에 해당하지 않으면 모든 문서를 매치합니다.
                        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
                        break;
                }
            }
        } else {
            // 쿼리가 null 또는 비어 있는 경우, 모든 문서를 매치합니다.
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
