package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomResDto;
import com.HealthCare.HealthyLife_Backend.entity.ChatRoom;
import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.ChatRoomRepository;
import com.HealthCare.HealthyLife_Backend.repository.ChattingRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper; // JSON 문자열로 변환하기 위한 객체
    private final ChattingRepository chattingRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private Map<String, ChatRoomResDto> chatRooms = new ConcurrentHashMap<>(); // 채팅방 정보를 담을 맵

    @PostConstruct // 의존성 주입 이후 초기화를 수행하는 메소드
    private void init() { // 채팅방 정보를 담을 맵을 초기화
        chatRooms = new LinkedHashMap<>(); // 채팅방 정보를 담을 맵
    }
    public ChatRoomResDto findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }
    // 1:1 채팅을위한 룸아이디 설정
    private String generateRoomId(Long memberId, Long senderId) {
        return (memberId < senderId) ? String.format("%d_%d", memberId, senderId) : String.format("%d_%d", senderId, memberId);
    }
    //방 생성 및 저장
    public ChatRoomResDto createOrEnterRoom(Long memberId, Long senderId) {
        String roomId = generateRoomId(memberId, senderId);
        // 해당 roomId로 이미 방이 생성되어 있는 경우
        if (chatRooms.containsKey(roomId)) {
            // 기존 방에 입장하는 메서드
            ChatRoomResDto existingRoom = chatRooms.get(roomId);
            return existingRoom;
        }
        // 방이 없는 경우, 새로 생성하여 저장
        ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                .roomId(roomId)
                .memberId(memberId)
                .senderId(senderId)
                .regDate(LocalDateTime.now())
                .build();
        // 방 정보를 맵에 등록
        chatRooms.put(roomId, chatRoom);
        // 데이터베이스에도 저장
        Member member = memberRepository.findById(memberId).orElseThrow();
        ChatRoom chatRoomEntity = ChatRoom.builder()
                .roomId(roomId)
                .member(member)
                .senderId(senderId)
                .regDate(LocalDateTime.now())
                .build();
        chatRoomRepository.save(chatRoomEntity);
        return chatRoom;
    }

    // 채팅룸 리스트 출력
    public List<ChatRoomResDto> chatRoomList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

        List<ChatRoom> allChatRooms = chatRoomRepository.findByMemberOrSenderId(member, memberId);

        return allChatRooms.stream()
                .map(chatRoom -> ChatRoomResDto.builder()
                        .roomId(chatRoom.getRoomId())
                        .regDate(chatRoom.getRegDate())
                        .memberId(chatRoom.getMember().getId())
                        .senderId(chatRoom.getSenderId())
                        .build())
                .collect(Collectors.toList());
    }

    //메세지 저장
    public void saveMessage(String roomId, Long senderId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));
        Chatting chatMessage = Chatting.builder()
                .chatRoom(chatRoom)
                .member(memberRepository.findById(senderId).orElseThrow())
                .message(message)
                .messageTime(LocalDateTime.now())
                .build();
        chattingRepository.save(chatMessage);
    }

// 전의 메세지 가져오기
    public List<ChatMessageDto> getRecentMessages(String roomId) {
        List<Chatting> recentMessages = chattingRepository.findRecentMessages(roomId);

        // Chatting 엔터티를 ChatMessageDto로 변환
        return recentMessages.stream()
                .map(ChatMessageDto::new)
                .collect(Collectors.toList());
    }

    // 세션 삭제하는 메서드, 해당 방이 세션을 포함하지 않으면 삭제
    public void removeRoom(String roomId) {
        ChatRoomResDto room = chatRooms.get(roomId); // 방 정보 가져오기
        if (room != null) { // 방이 존재하면
            if (room.isSessionEmpty()) { // 방에 세션이 없으면
                chatRooms.remove(roomId); // 방 삭제
            }
        }
    }
    // 채팅방에 입장한 세션 추가하는 메서드
    public void addSessionAndHandleEnter(String roomId, WebSocketSession session, ChatMessageDto chatMessage) {
        ChatRoomResDto room = findRoomById(roomId);
        if (room != null) {
            room.getSessions().add(session); // 채팅방에 입장한 세션 추가
            if (chatMessage.getSender() != null) { // 채팅방에 입장한 사용자가 있으면
                chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
                sendMessageToAll(roomId, chatMessage); // 채팅방에 입장 메시지 전송
            }
            log.debug("New session added: " + session);
        }
    }

    // 채팅방에서 퇴장한 세션 제거하는 메서드
    public void removeSessionAndHandleExit(String roomId, WebSocketSession session, ChatMessageDto chatMessage) {
        ChatRoomResDto room = findRoomById(roomId); // 채팅방 정보 가져오기
        if (room != null) {
            room.getSessions().remove(session); // 채팅방에서 퇴장한 세션 제거
            if (chatMessage.getSender() != null) { // 채팅방에서 퇴장한 사용자가 있으면
                chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");
                sendMessageToAll(roomId, chatMessage); // 채팅방에 퇴장 메시지 전송
            }
            log.debug("Session removed: " + session);
            if (room.isSessionEmpty()) {
                removeRoom(roomId);
            }
        }
    }

    // 채팅방의 모든 세션에 메시지를 가져오는 메서드
    public void sendMessageToAll(String roomId, ChatMessageDto message) {
        ChatRoomResDto room = findRoomById(roomId);
        if (room != null) {
            for (WebSocketSession session : room.getSessions()) {
                sendMessage(session, message);
            }
        }
    }

    // 특정 세션에 메시지를 전송하는 메서드
    // T는 제네릭 객체라 어떤 것도 보낼수있음
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
