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
        // 쿼리 전처리: "(주)" 제거


        // "통합" 검색 모드일 때
        if (query != null && !query.isEmpty()) {
            if ("통합".equals(filter)) {
                BoolQueryBuilder combinedQuery = QueryBuilders.boolQuery();
                if (query.contains("주")) {
                    // "주"를 검색할 때 특정 단어를 포함하지 않는 문서를 찾습니다.
                    combinedQuery.should(QueryBuilders.matchQuery("functionalities", query).boost(4));
                    combinedQuery.should(QueryBuilders.matchQuery("product_name", query).boost(3));
                    combinedQuery.should(QueryBuilders.matchQuery("company", query).boost(2));
                    combinedQuery.should(QueryBuilders.matchQuery("report_no", query).boost(1));
                    // 제외할 단어들에 대한 mustNot 쿼리 추가
                    combinedQuery.mustNot(QueryBuilders.matchQuery("company", "(주)"));
                    combinedQuery.mustNot(QueryBuilders.matchQuery("company", "주식회사"));
                    combinedQuery.mustNot(QueryBuilders.matchQuery("company", "회사"));
                    combinedQuery.mustNot(QueryBuilders.matchQuery("company", "주식"));
                } else {
                    // 일반적인 "통합" 검색 쿼리
                    combinedQuery.should(QueryBuilders.matchQuery("functionalities", query).boost(4));
                    combinedQuery.should(QueryBuilders.matchQuery("product_name", query).boost(3));
                    combinedQuery.should(QueryBuilders.matchQuery("company", query).boost(2));
                    combinedQuery.should(QueryBuilders.matchQuery("report_no", query).boost(1));
                }
                boolQueryBuilder.must(combinedQuery);
            } else {
                // 특정 필드에 대한 검색
                switch (filter) {
                    case "제품명":
                        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("product_name", query.trim().replace("\"", "")));
                        break;
                    case "제조사":
                        BoolQueryBuilder excludeJuQuery = QueryBuilders.boolQuery();
                        String processedQuery = query.trim().replace("\"", "")
                                .replace("(주)", "")
                                .replace("주식회사", "")
                                .replace("회사", "")
                                .replace("주식", "");
                        excludeJuQuery.should(QueryBuilders.matchPhrasePrefixQuery("company", processedQuery));
                        // 제외할 단어들에 대한 mustNot 쿼리 추가
                        excludeJuQuery.mustNot(QueryBuilders.matchPhraseQuery("company", "(주)"));
                        excludeJuQuery.mustNot(QueryBuilders.matchPhraseQuery("company", "주식회사"));
                        excludeJuQuery.mustNot(QueryBuilders.matchPhraseQuery("company", "회사"));
                        excludeJuQuery.mustNot(QueryBuilders.matchPhraseQuery("company", "주식"));
                        boolQueryBuilder.must(excludeJuQuery);
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
