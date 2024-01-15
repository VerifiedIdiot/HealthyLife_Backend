package com.HealthCare.HealthyLife_Backend.config;

import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Component
// WebSocketHandler를 상속받아서 WebSocketHandler를 구현
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper; // JSON 문자열로 변환하기 위한 객체
    private final ChatService chatService; // 채팅방 관련 비즈니스 로직을 처리할 서비스
    private final Map<WebSocketSession, String> sessionRoomIdMap = new ConcurrentHashMap<>(); // 세션과 채팅방 ID를 매핑할 맵

    @Override
    // 클라이언트가 서버로 연결을 시도할 때 호출되는 메서드
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload(); // 클라이언트가 전송한 메시지

        log.warn("{}", payload); // 수신된 메시지를 로깅

        // JSON 문자열을 ChatMessageDto 객체로 변환
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

        String roomId = chatMessage.getRoomId(); // 채팅방 ID

        // 세션과 채팅방 ID를 매핑
        sessionRoomIdMap.put(session, chatMessage.getRoomId());

        if (chatMessage.getType() == ChatMessageDto.MessageType.ENTER) {
            // 메시지 타입이 ENTER이면 채팅방에 입장한 세션 추가
            chatService.addSessionAndHandleEnter(roomId, session, chatMessage);
        } else if (chatMessage.getType() == ChatMessageDto.MessageType.CLOSE) {
            // 메시지 타입이 CLOSE이면 채팅방에서 세션 제거하고 퇴장 처리
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);
        } else {
            // 그 외의 경우에는 채팅방의 모든 참가자에게 메시지 전송
            chatService.sendMessageToAll(roomId, chatMessage);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션과 매핑된 채팅방 ID 가져오기
        log.warn("afterConnectionClosed : {}", session);

        // 세션과 매핑된 채팅방 ID를 가져옴
        String roomId = sessionRoomIdMap.remove(session);

        // 채팅방 ID가 존재하면
        if (roomId != null) {
            // 채팅 메시지 객체 생성 및 타입 설정 (CLOSE)
            ChatMessageDto chatMessage = new ChatMessageDto();
            chatMessage.setType(ChatMessageDto.MessageType.CLOSE);

            // 채팅방에서 세션 제거하고 퇴장 처리
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);
        }
    }
}