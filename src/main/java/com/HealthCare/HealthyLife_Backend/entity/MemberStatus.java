package com.HealthCare.HealthyLife_Backend.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "member_status")
public class MemberStatus {
    @Id
    @Column(name = "status_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String statusMessage; //상태 메세지
    private LocalDateTime lastAccessTime; //최근접속시간 예) 5분전 접속
    private String status; //현재 상태 예) 내상태 접속중,접속아님,바쁨,말걸지마세요,이런거?

    @OneToOne
    private Member member;


}
