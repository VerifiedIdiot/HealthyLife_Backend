package com.HealthCare.HealthyLife_Backend.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
public class ChatRoomResDto {
    private String roomId; // 채팅방 ID
    private String name; // 채팅방 이름
    private LocalDateTime regDate; // 채팅방 생성 시간
    private Long memberId;
    private Long senderId;
    @JsonIgnore // 이 어노테이션으로 WebSocketSession의 직렬화를 방지
    private Set<WebSocketSession> sessions; // 채팅방에 입장한 세션 정보를 담을 Set
    // 세션 수가 0인지 확인하는 메서드
    public boolean isSessionEmpty() {
        return this.sessions.size() == 0;
    }



    public ChatRoomResDto(String roomId, String name, LocalDateTime regDate ,Long memberId,Long senderId) {
        this.roomId = roomId;
        this.name = name;
        this.memberId =memberId;
        this.senderId=senderId;
        this.regDate = regDate;
        this.sessions = Collections.newSetFromMap(new ConcurrentHashMap<>()); // 동시성 문제를 해결하기 위해 ConcurrentHashMap 사용
    }
}