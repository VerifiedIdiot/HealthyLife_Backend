package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long commentId;
    private Long communityId;
    private LocalDateTime regDate;
    private String content;

}