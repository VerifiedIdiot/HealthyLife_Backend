package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.enums.Authority;
import com.HealthCare.HealthyLife_Backend.enums.ExercisePurpose;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "isKakao", columnDefinition = "TINYINT(1)")
    private boolean isKakao;
    @Column(name = "birth", nullable = false)
    private String birth;
    @Column(name = "exerciseInfo", nullable = false)
    private String exerciseInfo;

    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private ExercisePurpose exercisePurpose; // 새로운 운동 목적 추가

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Body> bodies;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<SeasonRanking> seasonRankings;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private TotalRanking totalRanking;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calendar> calendars;


    private LocalDateTime regDate;
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

    @Builder
    public Member(String email, String password, String name, String nickName, String gender, String phone
            , String addr, String image, boolean isKakao, String birth, String exerciseInfo ,Authority authority, ExercisePurpose exercisePurpose) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
        this.phone = phone;
        this.addr = addr;
        this.image = image;
        this.isKakao = isKakao;
        this.birth = birth;
        this.exerciseInfo = exerciseInfo;
        this.regDate = LocalDateTime.now();
        this.authority = authority;
        this.exercisePurpose = exercisePurpose; // 새로운 운동 목적 추가
    }
}
