package com.HealthCare.HealthyLife_Backend.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
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

}
