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
    private Long myId;
    private Long friendsId;
    private String status; // 친구, 차단 상태
}
