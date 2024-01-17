package com.HealthCare.HealthyLife_Backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communityId")
    private Community community;

    private LocalDateTime regDate;

    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

    @Column(length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
    private List<Comment> childComments = new ArrayList<>();

    @Column(name = "ipAddress")
    private String ipAddress;
    private String email;
    private String Name;
    private String password;}

