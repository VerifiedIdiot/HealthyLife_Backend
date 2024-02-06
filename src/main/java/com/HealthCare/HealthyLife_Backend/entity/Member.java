package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.enums.Authority;
import com.HealthCare.HealthyLife_Backend.enums.ExercisePurpose;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "member_tb")
public class Member implements Serializable {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "nickName", nullable = false, unique = true)
    private String nickName;
    @Column(name = "gender", nullable = false)
    private String gender;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "addr", nullable = false)
    private String addr;
    private String image;
    @Column(name = "isAuthDelete", columnDefinition = "TINYINT(1)")
    private boolean isAuthDelete;
    private LocalDate birth;
    private LocalDateTime regDate;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private ExercisePurpose exercisePurpose; // 새로운 운동 목적 추가

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Body> bodies;
    @OneToMany(mappedBy = "member", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<SeasonRanking> seasonRankings;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private MemberStatus memberStatus;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Friend> friends;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Chatting> chattings;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Community> communities;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments;


    @PrePersist
    protected void prePersist() {
        regDate = LocalDateTime.now();
    }

    @Builder
    public Member(String email, String password, String name, String nickName, String gender, String phone
            , String addr, String image, LocalDate birth, boolean isAuthDelete, Authority authority, ExercisePurpose exercisePurpose) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
        this.phone = phone;
        this.addr = addr;
        this.image = image;
        this.birth = birth;
        this.regDate = LocalDateTime.now();
        this.isAuthDelete = isAuthDelete;
        this.authority = authority;
        this.exercisePurpose = exercisePurpose; // 새로운 운동 목적 추가
    }
}
