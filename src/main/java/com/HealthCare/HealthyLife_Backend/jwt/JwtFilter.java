package com.HealthCare.HealthyLife_Backend.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor // final이 붙은 필드를 인자값으로 하는 생성자를 만들어줌
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // 토큰을 요청 헤더의 Authorization 키에 담아서 전달
    public static final String BEARER_PREFIX = "Bearer "; // 토큰 앞에 붙는 문자열
    private final TokenProvider tokenProvider; // 토큰 생성, 토큰 검증을 수행하는 TokenProvider

    private String resolveToken(HttpServletRequest request) { // 토큰을 요청 헤더에서 꺼내오는 메서드
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // 헤더에서 토큰 꺼내오기
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) { // 토큰이 존재하고, 토큰 앞에 붙는 문자열이 존재하면
            return bearerToken.substring(7); // 토큰 앞에 붙는 문자열을 제거하고 토큰 반환
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 클라이언트로부터 전달된 HTTP 요청에서 토큰 추출
        String jwt = resolveToken(request);
        // 추출된 토큰이 비어 있지 않고, 토큰의 유효성이 검증되면
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 토큰을 사용하여 Authentication 객체 생성
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // SecurityContext에 Authentication 객체를 설정하여 현재 사용자를 인증 상태로 만듬
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
