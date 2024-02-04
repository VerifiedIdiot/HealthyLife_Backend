package com.HealthCare.HealthyLife_Backend.entity;


import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
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
@Table(name = "chatting_tb")
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chatting_id")
    private Long id;
    private String type;
    @Column(name = "message",nullable = false)
    private String message; //메세지
    @Column(name = "message_time",nullable = false)
    private LocalDateTime messageTime; //메세지 생성 시간

    @PrePersist
    protected void prePersist() {
        messageTime = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "member_id") // 외래키
    private Member member; // 본인

    @ManyToOne(fetch = FetchType.LAZY) // 지연 전략
    @JoinColumn(name = "room_id") // 외래키
    private ChatRoom chatRoom; // 채팅룸

    @Column(name = "message_status" )
    private String messageStatus; //메세지 읽음,안읽음

    public Chatting build() {
        Chatting chatting = new Chatting();
        chatting.setMessage(this.message);
        chatting.setMessageTime(this.messageTime);
        chatting.setMember(this.member);
        chatting.setChatRoom(this.chatRoom);
        chatting.setMessageStatus(this.messageStatus);
        return chatting;
    }
}
