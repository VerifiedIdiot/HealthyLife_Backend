package com.HealthCare.HealthyLife_Backend.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "chat_room_tb")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_room_id")
    private Long id;
    @Column(name = "room_id",nullable = false)
    private String roomId; // 채팅방 ID
    @Column(name = "room_name",nullable = false)
    private String name; // 채팅방 이름
    private LocalDateTime regDate; // 채팅방 생성 시간
    private Long senderId; // 상대 정보 아이디

    @PrePersist
    protected void prePersist() {
        regDate = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "member_id") // 외래키
    private Member member; // 방 주인

    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL)
    private List<Chatting> chatting;

}
