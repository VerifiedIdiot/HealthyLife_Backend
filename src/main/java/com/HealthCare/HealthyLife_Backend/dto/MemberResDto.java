package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResDto {
    private long id;
    private String email;
    private String name;
    private String alias;
    private String gender;
    private String phone;
    private String addr;
    private String image;
    private LocalDate birth;
    private LocalDateTime regDate;

    // MemberResDto -> Member
    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .alias(member.getAlias())
                .gender(member.getGender())
                .phone(member.getPhone())
                .addr(member.getAddr())
                .image(member.getImage())
                .birth(member.getBirth())
                .build();
    }
}
