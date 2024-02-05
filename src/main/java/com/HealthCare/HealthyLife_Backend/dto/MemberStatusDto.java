package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberStatusDto {
    private String statusMessage; //상태 메세지
    private LocalDateTime lastAccessTime; //최근접속시간 예) 5분전 접속
    private String status; //현재 상태 예) 내상태 접속중,접속아님,바쁨,말걸지마세요,이런거?
    private Member memberId;
}
