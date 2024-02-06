package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.service.AuthService;
import com.HealthCare.HealthyLife_Backend.service.KakaoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@RequestMapping("/auth")
public class KakaoController {
    private final KakaoService kakaoService;
    private final AuthService authService;

    @GetMapping("/kakao/{code}")
    public ResponseEntity<String> kakao(@PathVariable String code) {
        log.info("code {} : ", code);
        String email = kakaoService.kakaoToken(code);
        return ResponseEntity.ok(email);
    }
}
