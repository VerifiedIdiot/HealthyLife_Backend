package com.HealthCare.HealthyLife_Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ml/**")  // 여기에는 실제 사용하는 엔드포인트 패턴을 넣어주세요
                .allowedOrigins("http://localhost:3000")  // 허용할 오리진 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드 설정
                .allowCredentials(true)  // 인증 정보를 포함할지 여부 설정
                .maxAge(3600);  // 최대 허용 시간 설정 (초 단위)
    }
}
