package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomResDto;
import com.HealthCare.HealthyLife_Backend.entity.ChatRoom;
import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.ChatRoomRepository;
import com.HealthCare.HealthyLife_Backend.repository.ChattingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper; // JSON 문자열로 변환하기 위한 객체
    private final ChatRoomRepository chatRoomRepository;
    private final ChattingRepository chattingRepository;
    private Map<String, ChatRoomResDto> chatRooms; // 채팅방 정보를 담을 맵

    @PostConstruct // 의존성 주입 이후 초기화를 수행하는 메소드
    private void init() { // 채팅방 정보를 담을 맵을 초기화
        List<ChatRoom> chatRoomEntityList = chatRoomRepository.findAll();
        for (ChatRoom chatRoomEntity : chatRoomEntityList) {
            ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                    .roomId(chatRoomEntity.getRoomId())
                    .name(chatRoomEntity.getName())
                    .memberId(chatRoomEntity.getMember().getId()) //채팅방주인
                    .senderId(chatRoomEntity.getSenderId()) //채팅이용자
                    .regDate(chatRoomEntity.getRegDate())
                    .build();
            chatRooms.put(chatRoomEntity.getRoomId(), chatRoom);
        }
    }
        // 전체 채팅방 리스트
        public List<ChatRoomResDto> findAllRoom() {
            return new ArrayList<>(chatRooms.values());
        }
        // 회원별 채팅방 리스트
        public List<ChatRoomResDto> findRoomForMember(Member member) {
            List<ChatRoomResDto> chatRoomList = new ArrayList<>();

            for (ChatRoomResDto chatRoom : chatRooms.values()) {
                // 예시: ChatRoomResDto에 있는 Member와 비교하여 특정 조건을 만족하는지 확인
                if (chatRoom.getMemberId().equals(member.getId())) {
                    chatRoomList.add(chatRoom);
                }
                if (chatRoom.getSenderId().equals(member.getId())) {
                    chatRoomList.add(chatRoom);
                }
            }
            return chatRoomList;
        }

        // 방 찾기 (필요할수도있어서넣음)
        public ChatRoomResDto findRoomById(String roomId) {
            return chatRooms.get(roomId);
        }
        public void sendMessageToAll(String roomId, ChatMessageDto message) {
        }

        // 특정 세션에 메시지를 전송하는 메서드
        public <T> void sendMessage(WebSocketSession session, T message) {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        // 메세지 저장
        public void saveMessage(Long roomId, Member member, String message) {
            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));
            Chatting chatMessage = new Chatting();
            chatMessage.setChatRoom(chatRoom);
            chatMessage.setMember(member);
            chatMessage.setMessage(message);
            chatMessage.setMessageTime(LocalDateTime.now());
            chattingRepository.save(chatMessage);
        }
        // 이전 채팅 가져오기
        public List<Chatting> getRecentMessages(String roomId) {
            return chattingRepository.findRecentMessages(roomId);
        }
//    // 세션 수 가져오기
//    public int getSessionCount(String roomId) {
//        List<WebSocketSession> sessions = roomSessions.get(roomId);
//        return sessions != null ? sessions.size() : 0;
//    }
    }
