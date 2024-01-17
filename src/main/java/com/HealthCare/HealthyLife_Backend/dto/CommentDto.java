package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long communityId;
    private String email;
    private String content;
    private LocalDateTime regDate;
    private Long parentCommentId;
    private List<CommentDto> childComments;
    private String name;
    private String password;
    private String ipAddress;
}
