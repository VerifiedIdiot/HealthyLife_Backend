package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType; // 인증방식
    private String accessToken; // 액세스 토큰
    private String refreshToken; // 리프레시 토큰
    private Long accessTokenExpiresIn; // 액세스 토큰 만료 시간
    private Long refreshTokenExpiresIn; // 리프레시 토큰 만료 시간
}
