package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "like_it_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@ToString
@NoArgsConstructor
public class CommunityLikeIt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_it_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(name = "is_like_it")
    private boolean isLikeIt; // true일 경우 추천, false일 경우 비추천
    @Column(name = "email")
    private String email;

    @Builder
    public CommunityLikeIt(Community community, boolean isLikeIt, String email) {
        this.community = community;
        this.isLikeIt = isLikeIt;
        this.email = email;
    }

    // 빌더 패턴을 유지하면서 추천/비추천 여부를 변경하는 메서드
    public CommunityLikeIt toBuilder(boolean isLikeIt) {
        return CommunityLikeIt.builder()
                .community(this.community)
                .isLikeIt(isLikeIt)
                .email(this.email)
                .build();
    }


}

