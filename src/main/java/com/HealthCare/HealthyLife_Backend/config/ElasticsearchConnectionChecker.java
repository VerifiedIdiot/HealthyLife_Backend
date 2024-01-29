package com.HealthCare.HealthyLife_Backend.config;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true")
public class ElasticsearchConnectionChecker implements ApplicationRunner {

    private final RestHighLevelClient client;

    @Autowired
    public ElasticsearchConnectionChecker(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Elasticsearch 서버에 대한 간단한 헬스 체크를 수행
            if (client.ping(RequestOptions.DEFAULT)) {
                System.out.println("Elasticsearch 서버에 성공적으로 연결되었습니다.");
            } else {
                System.out.println("Elasticsearch 서버가 응답하지 않습니다.");
            }
        } catch (IOException e) {
            System.out.println("Elasticsearch 서버에 연결할 수 없습니다: " + e.getMessage());
        }
    }
}

