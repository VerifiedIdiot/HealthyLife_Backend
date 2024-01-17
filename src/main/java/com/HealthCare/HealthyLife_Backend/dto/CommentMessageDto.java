package com.HealthCare.HealthyLife_Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentMessageDto {
    public enum MessageType {
        COMMENT,
        INIT
    }
    private String postId;
    private String commentId;
    private String commentContent;
    private String commenterEmail;
    private String authorEmail;
}

