package com.HealthCare.HealthyLife_Backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "like_it") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityLikeIt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(name = "is_like_it")
    private boolean isLikeIt; // true일 경우 추천, false일 경우 비추천
    @Column(name = "email")
    private String email;
}

