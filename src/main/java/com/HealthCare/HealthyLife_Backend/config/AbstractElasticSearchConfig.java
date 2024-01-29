package com.HealthCare.HealthyLife_Backend.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;


// 엘라스틱서치와 연동을 위한 기본설정을 제공하는 추상클래스
public abstract class AbstractElasticSearchConfig extends ElasticsearchConfigurationSupport {
    //RestHighLevelClient은 엘라스틱서치와 통신을 담당하는 클라이언트 객체
    @Bean
    public abstract RestHighLevelClient elasticsearchClient();

    //ElasticsearchOperations은 RestTemplate과 같이 엘라스틱서치와의 송신하기위핫 템픗릿과 함수를 제공해준다.
    //ElasticsearchConverter는 자바객체와 엘라스틱서치객체 사이의 변환을 처리한다
    @Bean(name = { "elasticsearchOperations", "elasticsearchTemplate" })
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchConverter elasticsearchConverter,
                                                           RestHighLevelClient elasticsearchClient) {
        ElasticsearchRestTemplate template = new ElasticsearchRestTemplate(elasticsearchClient, elasticsearchConverter);
        template.setRefreshPolicy(refreshPolicy());
        return template;
    }
}
