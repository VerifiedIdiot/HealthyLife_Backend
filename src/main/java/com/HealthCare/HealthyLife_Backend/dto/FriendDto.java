package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDto {
    private Long friendsId; //친구
    private Boolean status; // 친구일떄 true , 차단일때 false 상태
    private Long memberId; // 본인
}
