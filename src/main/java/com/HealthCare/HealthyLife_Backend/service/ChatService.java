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
    private final MemberRepository memberRepository;
    private Map<String, ChatRoomResDto> chatRooms; // 채팅방 정보를 담을 맵

    @PostConstruct // 의존성 주입 이후 초기화를 수행하는 메소드
    private void init() {
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
//        // 전체 채팅방 리스트
//        public List<ChatRoomResDto> findAllRoom() {
//            return new ArrayList<>(chatRooms.values());
//        }
        // 회원별 채팅방 리스트
        public List<ChatRoomResDto> findRoomForMember(Long memberId) {
            List<ChatRoomResDto> chatRoomList = new ArrayList<>();

            for (ChatRoomResDto chatRoom : chatRooms.values()) {
                // 예시: ChatRoomResDto에 있는 Member와 비교하여 특정 조건을 만족하는지 확인
                if (chatRoom.getMemberId().equals(memberId)) {
                    chatRoomList.add(chatRoom);
                }
                if (chatRoom.getSenderId().equals(memberId)) {
                    chatRoomList.add(chatRoom);
                }
            }
            return chatRoomList;
        }

        // 방 찾기 (필요할수도있어서넣음)
        public ChatRoomResDto findRoomById(String roomId) {
            return chatRooms.get(roomId);
        }
//        public void sendMessageToAll(String roomId, ChatMessageDto message) {
//        }

    // 특정 세션에 메시지를 전송하는 메서드
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            // ObjectMapper를 사용하여 메시지 객체를 JSON 형식의 문자열로 변환
            String jsonMessage = objectMapper.writeValueAsString(message);

            // WebSocket 세션에 JSON 형식의 문자열을 포함한 TextMessage를 전송
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (IOException e) {
            // 예외가 발생하면 로그에 오류 메시지를 기록
            log.error(e.getMessage(), e);
        }
    }

    // 메세지 저장
    public void saveMessage(String roomId, Long memberId, String message) {
        // 주어진 roomId를 사용하여 채팅방을 조회하고, 존재하지 않을 경우 RuntimeException 발생
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));
        Member member1 =memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        // Chatting 엔터티 생성 및 필드 설정
        Chatting chatMessage = new Chatting();
        chatMessage.setChatRoom(chatRoom);    // 채팅 메시지가 속한 채팅방 설정
        chatMessage.setMember(member1);        // 채팅 메시지를 작성한 회원 설정
        chatMessage.setMessage(message);      // 채팅 메시지 내용 설정
        chatMessage.setMessageTime(LocalDateTime.now());  // 채팅 메시지 작성 시간 설정

        // 생성된 채팅 메시지를 저장소에 저장
        chattingRepository.save(chatMessage);
    }

    // 이전 채팅 가져오기
    public List<Chatting> getRecentMessages(String roomId) {
        // chattingRepository를 사용하여 주어진 채팅방 ID에 해당하는 최근 채팅 메시지 목록을 조회하여 반환
        return chattingRepository.findRecentMessages(roomId);
    }

    // 방 개설하기
    public ChatRoomResDto createRoom(String name, Long memberId, Long senderId) {
        String randomId = UUID.randomUUID().toString();
        log.info("UUID : " + randomId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ChatRoom chatRoomEntity = ChatRoom.builder()
                .roomId(randomId)
                .member(member)
                .senderId(senderId)
                .name(name)
                .regDate(LocalDateTime.now())
                .build();
        chatRoomRepository.save(chatRoomEntity);
        ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                .roomId(randomId)
                .name(name)
                .regDate(LocalDateTime.now())
                .memberId(memberId)
                .senderId(senderId)
                .build();

        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

//    // 세션 수 가져오기
//    public int getSessionCount(String roomId) {
//        List<WebSocketSession> sessions = roomSessions.get(roomId);
//        return sessions != null ? sessions.size() : 0;
//    }
    }
