package com.HealthCare.HealthyLife_Backend.security;

import com.HealthCare.HealthyLife_Backend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
//WebSecurityConfig : 스프링 시큐리티에 필요한 설정
public class WebSecurityConfig implements WebMvcConfigurer {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화 객체를 Bean으로 등록
    }
    @Bean // SecurityFilterChain 객체를 Bean으로 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic() // HTTP Basic Authentication을 설정합니다.
                .and()
                .csrf().disable() // CSRF 보호를 비활성화합니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리 설정을 합니다. 여기서는 STATELESS로 설정하여 세션을 사용하지 않도록 합니다.
                .and()
                .exceptionHandling()// 예외 처리를 설정합니다. jwtAuthenticationEntryPoint와 jwtAccessDeniedHandler를 사용하여 인증 및 권한 예외를 처리합니다.
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .authorizeRequests()
                //antMatchers: 특정 경로에 대한 접근 권한을 설정
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**").permitAll() //스웨거 권한
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated() // 나머지 요청에 대해서는 인증된 사용자만 허용합니다.
                .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // JwtSecurityConfig를 적용합니다. 이 클래스는 커스텀한 JwtFilter를 설정에 추가

        return http.build();
    }
    @Override // 메소드 오버라이딩, localhost:3000번으로 들어오는 요청 허가
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}