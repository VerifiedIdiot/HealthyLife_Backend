package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
public class CommentDto {
    private Long commentId;
    private Long communityId;
    private String email;
    private String nickName;
    private LocalDateTime regDate;
    private String content;

}