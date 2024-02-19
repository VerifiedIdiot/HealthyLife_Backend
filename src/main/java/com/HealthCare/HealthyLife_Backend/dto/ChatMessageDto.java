package com.HealthCare.HealthyLife_Backend.dto;

import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter @Builder @AllArgsConstructor
public class ChatMessageDto {
    public enum MessageType {
        ENTER, TALK, CLOSE
    }
    private MessageType type;
    private String roomId;
    private Long sender;
    private String message;
    private LocalDateTime messageTime;
    private String messageStatus; // 메세지 읽음, 안읽음


    public ChatMessageDto(Chatting chatting) {
        this.type = MessageType.TALK; // Assuming it's always a talk type
        this.roomId = chatting.getChatRoom().getMember().getId().toString();
        this.sender = chatting.getMember().getId();
        this.message = chatting.getMessage();
        this.messageTime = chatting.getMessageTime();
        this.messageStatus = chatting.getMessageStatus(); // 이 줄을 추가하세요
    }


}