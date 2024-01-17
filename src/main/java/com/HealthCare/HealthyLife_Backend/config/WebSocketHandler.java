package com.HealthCare.HealthyLife_Backend.config;

import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.dto.CommentMessageDto;
import com.HealthCare.HealthyLife_Backend.enums.MessageType;
import com.HealthCare.HealthyLife_Backend.service.ChatService;
import com.HealthCare.HealthyLife_Backend.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
// WebSocketHandler를 상속받아서 WebSocketHandler를 구현
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper; // JSON 문자열로 변환하기 위한 객체
    private final ChatService chatService; // 채팅방 관련 비즈니스 로직을 처리할 서비스

    @Getter
    private final Map<String, List<WebSocketSession>> userSessionMap = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionRoomIdMap = new ConcurrentHashMap<>(); // 세션과 채팅방 ID를 매핑할 맵

    @Override
    // 클라이언트가 서버로 연결을 시도할 때 호출되는 메서드
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try{
        String payload = message.getPayload(); // 클라이언트가 전송한 메시지

        log.warn("{}", payload); // 수신된 메시지를 로깅

        // CommentService를 통해 댓글 메시지 처리
        CommentMessageDto commentMessage = objectMapper.readValue(payload, CommentMessageDto.class);
        // JSON 문자열을 ChatMessageDto 객체로 변환
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

        String roomId = chatMessage.getRoomId(); // 채팅방 ID

        // 세션과 채팅방 ID를 매핑
        sessionRoomIdMap.put(session, chatMessage.getRoomId());

        if (chatMessage.getType() == MessageType.ENTER) {
            // 메시지 타입이 ENTER이면 채팅방에 입장한 세션 추가
            chatService.addSessionAndHandleEnter(roomId, session, chatMessage);
        } else if (chatMessage.getType() == MessageType.CLOSE) {
            // 메시지 타입이 CLOSE이면 채팅방에서 세션 제거하고 퇴장 처리
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);
        } else {
            // 그 외의 경우에는 채팅방의 모든 참가자에게 메시지 전송
            chatService.sendMessageToAll(roomId, chatMessage);
        }
    } catch (Exception e) {
        log.error("Error handling message: ", e);
    }
}
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션과 매핑된 채팅방 ID 가져오기
        log.warn("afterConnectionClosed : {}", session);
        String identifier = getEmailFromSession(session);
        if (identifier == null) {
            identifier = Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress();
        }
        List<WebSocketSession> sessions = userSessionMap.get(identifier);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                userSessionMap.remove(identifier);
            }
        }
        // 세션과 매핑된 채팅방 ID를 가져옴
        String roomId = sessionRoomIdMap.remove(session);

        // 채팅방 ID가 존재하면
        if (roomId != null) {
            // 채팅 메시지 객체 생성 및 타입 설정 (CLOSE)
            ChatMessageDto chatMessage = new ChatMessageDto();
            chatMessage.setType(MessageType.CLOSE);

            // 채팅방에서 세션 제거하고 퇴장 처리
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);
        }
    }

    private String getEmailFromSession(WebSocketSession session) {
        try {
            String query = session.getUri().getQuery();
            if (query == null || query.isEmpty()) {
                return null;
            }
            Map<String, String> queryMap = Arrays.stream(query.split("&"))
                    .map(param -> param.split("="))
                    .filter(arr -> arr.length == 2)
                    .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
            String email = queryMap.get("email");

            // 이메일이 없는 경우 IP 주소를 반환합니다.
            if (email == null || email.isEmpty()) {
                return Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress();
            }
            return email;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 예외가 발생한 경우에도 IP 주소를 반환합니다.
        return Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress();
    }

}
