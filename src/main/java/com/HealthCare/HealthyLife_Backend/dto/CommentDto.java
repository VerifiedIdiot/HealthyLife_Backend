package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long communityId;
    private String email;
    private String nickName;
    private LocalDateTime regDate;
    private String content;

}