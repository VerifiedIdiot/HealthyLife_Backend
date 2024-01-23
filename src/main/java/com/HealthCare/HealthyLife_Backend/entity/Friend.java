package com.HealthCare.HealthyLife_Backend.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "Friend")
public class Friend {
    @Id
    @Column(name = "friend_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "friends_id",nullable = false)
    private Long friendsId;
    @Column(name = "status",nullable = false)
    private Boolean status; // 친구일떄 true , 차단일때 false 상태

    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "member_id") // 외래키
    private Member member; // 본인

}
