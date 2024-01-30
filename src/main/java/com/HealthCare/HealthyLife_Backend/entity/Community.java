package com.HealthCare.HealthyLife_Backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Builder
@NoArgsConstructor
@ToString
public class Community {
    @Id
    @Column(name = "community_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long communityId;
    private String title;
    // 문자 길이 지정 안하면 기본 varchar(255)
    @Lob
    private String content;
    //  사진은 더많은 자리수를 사용함 @Lob 사용시 적절하게 저장 가능
    @Lob
    private String text;

    private LocalDateTime regDate;

    //    엔티티가 생성될때마다 실행
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

    //    직렬화 시 해당 필드에 포함시키고 싶지 않을 때 선언하는 어노테이션 Response 데이터에서 해당 필드 제외

    //  여러 커뮤니티가 사용자 한명에서 사용 될 수 있다 : ManyToOne
//    LAZY-지연로딩 / Member를 조회하는 시점에 쿼리가 나감
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityLikeIt> communityLikeIts = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리
    private int likeCount;
    private int viewCount;
    private String categoryName;
    private String email;
    private String nickName;
    private String password;

    // Board와 Comment는 1:N 관계, mappedBy는 연관관계의 주인이 아니다(난 FK가 아니에요)라는 의미
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; // 댓글 목록

    @Builder
    public Community(Long communityId, String title, String content, String text, LocalDateTime regDate,
                     Member member, List<CommunityLikeIt> communityLikeIts, Category category,
                     int likeCount, int viewCount, String categoryName, String email, String nickName, String password,
                     List<Comment> comments) {
        this.communityId = communityId;
        this.title = title;
        this.content = content;
        this.text = text;
        this.regDate = regDate;
        this.member = member;
        this.communityLikeIts = communityLikeIts;
        this.category = category;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.categoryName = categoryName;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.comments = comments;
    }
}
