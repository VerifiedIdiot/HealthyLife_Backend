package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.dto.TokenDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.jwt.TokenProvider;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // member 이메일, 닉네임, 전화번호 중복 확인
    public Boolean checkUnique(int type, String info) {
        boolean isUnique;
        switch (type) {
            case 0 :
                isUnique = memberRepository.existsByEmail(info);
                break;
            case  1 :
                isUnique = memberRepository.existsByNickName(info);
                break;
            case  2 :
                isUnique = memberRepository.existsByPhone(info);
                break;
            default: isUnique = true;
            log.info("중복체크를 완료 하였습니다.");
        }
        return isUnique;
    }
    // member 회원 가입
    public MemberResDto signup(MemberReqDto memberReqDto) {
        if (memberRepository.existsByEmail(memberReqDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        Member member = memberReqDto.toEntity(passwordEncoder);
        return MemberResDto.of(memberRepository.save(member));
    }

    // member 로그인
    public TokenDto login(MemberReqDto memberReqDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = memberReqDto.toAuthentication();
            log.info("authenticationToken: {}", authenticationToken);

            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            log.info("authentication: {}", authentication);

            return tokenProvider.generateTokenDto(authentication);
        } catch (Exception e) {
            log.error("로그인 중 에러발생 : ", e);
            throw new RuntimeException("로그인 중 에러 발생", e);
        }
    }

    // accessToken 재발급
    public String createAccessToken(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        return tokenProvider.generateAccessToken(authentication);
    }

}
