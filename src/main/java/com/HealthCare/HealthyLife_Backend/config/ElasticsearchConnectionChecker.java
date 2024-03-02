package com.HealthCare.HealthyLife_Backend.config;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 스프링어플리케이션 실행시 엘라스틱서치에 ping을 날려서 연동됐는지 로그로 찍기위한 클래스
// AWS의 헬시책을 생각하면 되겠다
// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@Component
//@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
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
        } catch (Exception e) {
            System.out.println("Elasticsearch 서버에 연결할 수 없습니다: " + e.getMessage());
        }
    }
}

