package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.enums.Authority;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberReqDto {
    private long id;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private String gender;
    private String phone;
    private String addr;
    private String image;
    private Boolean isKakao;
    private String birth;
    private LocalDateTime regDate;

    // MemberReqDto -> Member MemberReqDto를 Member Entity로 변환
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .nickName(nickName)
                .gender(gender)
                .phone(phone)
                .addr(addr)
                .image(image)
                .isKakao(isKakao)
                .birth(birth)
                .regDate(regDate)  // 기본값 설정
                .authority(Authority.ROLE_USER)
                .build();
    }
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}