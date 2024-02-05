package com.HealthCare.HealthyLife_Backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "member_status_tb")
public class MemberStatus{
    @Id
    @Column(name = "member_status_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String statusMessage; //상태 메세지
    private LocalDateTime lastAccessTime; //최근접속시간 예) 5분전 접속
    private String status; //현재 상태 예) 내상태 접속중,접속아님,바쁨,말걸지마세요,이런거?
    @JsonIgnore
    @OneToOne
    private Member member;
}
