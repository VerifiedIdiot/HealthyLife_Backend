package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Comment;
import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private boolean isChecked;
    private Long memberId;
    private Long communityId;
    private Long commentId;

    public Notification toNotificationEntity(Member member, Community community, Comment comment) {
        return Notification.builder()
                .id(this.id)
                .isChecked(this.isChecked)
                .member(member)
                .community(community)
                .comment(comment)
                .build();
    }

}