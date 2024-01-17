package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {
    private Long id;
    private String email;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String content;
    private String text;
    private LocalDateTime regDate;
    private int viewCount;
    private int pickCount;
    private String ipAddress;
    private String name;
    private String password;

}