package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommunityDto {
    private Long communityId;
    private String title;
    private String content; // 사진
    private String text; // 내용
    private LocalDateTime regDate;
    private int likeItCount;
    private int viewCount;
    private String categoryName;
    private String email;
    private String nickName;
    private String password;


}