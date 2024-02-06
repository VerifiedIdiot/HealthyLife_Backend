package com.HealthCare.HealthyLife_Backend;


import com.HealthCare.HealthyLife_Backend.repository.ElasticsearchCrudRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

// 엘라스틱서치 서버가 켜져있지않으면 스프링이 죽어버려요 ..

@SpringBootApplication
@EnableElasticsearchRepositories
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = ElasticsearchCrudRepository.class))

//@EnableJpaRepositories

@EnableScheduling
public class HealthyLifeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthyLifeBackendApplication.class, args);
	}

}
