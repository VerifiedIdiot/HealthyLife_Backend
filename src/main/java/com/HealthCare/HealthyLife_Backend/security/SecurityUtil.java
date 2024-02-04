package com.HealthCare.HealthyLife_Backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
// SecurityUtil : SecurityContext에서 전역으로 유저 정보를 제공하는 유틸 클래스
public class SecurityUtil {
    private SecurityUtil() {
    }
    // Security Context의 Authentication 객체를 이용해 회원의 정보를 가져온다.
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        String userId = authentication.getName();
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            log.error("사용자 ID를 Long으로 변환하는 중 에러 발생: {}", userId);
            // 적절한 예외 처리 또는 기본값 설정 등을 수행할 수 있습니다.
            throw new RuntimeException("사용자 ID를 가져오는 중 에러 발생");
        }
    }
}
