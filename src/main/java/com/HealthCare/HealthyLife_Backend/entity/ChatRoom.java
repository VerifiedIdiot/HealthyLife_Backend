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
@Table(name = "ChatRoom")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String roomId; // 채팅방 ID
    private String name; // 채팅방 이름
    private LocalDateTime regDate; // 채팅방 생성 시간

}
