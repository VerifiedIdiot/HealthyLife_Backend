package com.HealthCare.HealthyLife_Backend.config;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import lombok.Builder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import static java.util.Collections.singletonMap;
import static org.elasticsearch.common.Priority.IMMEDIATE;


// 엘라스틱서치의 실제 연결정보와 클라이언트 생성로직을 구현하는곳

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${spring.elasticsearch.rest.uri}")
    private String esUri;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration =
                ClientConfiguration
                        .builder()
                        .connectedTo(esUri)
                        .build();
        return RestClients
                .create(clientConfiguration)
                .rest();
    }


}