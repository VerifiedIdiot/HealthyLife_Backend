package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "like_it_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityLikeIt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_it_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_like_it")
    private boolean isLiked; // true일 경우 추천, false일 경우 비추천

}