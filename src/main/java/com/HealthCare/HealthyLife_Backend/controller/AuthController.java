package com.HealthCare.HealthyLife_Backend.controller;


import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.service.AuthService;
import com.HealthCare.HealthyLife_Backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;


@Slf4j
@RestController
@Controller
@RequestMapping("/auth")
@CrossOrigin(origins = CORS_ORIGIN)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResDto> signup(@RequestBody MemberReqDto memberReqDto) {
        return ResponseEntity.ok(authService.signup(memberReqDto));
    }
}
