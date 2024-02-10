package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomReqDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomResDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.entity.ChatRoom;
import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;
@Slf4j
@RequiredArgsConstructor
@RestController
//@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    // 메세지 저장하기
    @PostMapping("/message")
    public ResponseEntity<Void> saveMessage(@RequestBody ChatMessageDto chatMessageDTO) {
        chatService.saveMessage(chatMessageDTO.getRoomId(), chatMessageDTO.getSender(), chatMessageDTO.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // HTTP POST 요청을 통해 새로운 채팅방을 생성하는 메서드
    @PostMapping("/new")
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomReqDto chatRoomDto) {
        log.warn("chatRoomDto : {}", chatRoomDto); // 전송된 ChatRoomReqDto 로그 기록
        ChatRoomResDto room = chatService.createOrEnterRoom(chatRoomDto.getMemberId(),chatRoomDto.getSenderId()); // 채팅 서비스를 통해 채팅방 생성
        System.out.println(room.getRoomId()); // 생성된 채팅방의 ID를 콘솔에 출력
        return ResponseEntity.ok(room.getRoomId()); // 생성된 채팅방의 ID를 응답으로 반환
    }

    // HTTP GET 요청을 통해 회원 채팅방 리스트를 조회하는 메서드
    @GetMapping("/list/{memberId}")
    public ResponseEntity<List<ChatRoomResDto>> findRoomList(@PathVariable Long memberId) {
        return ResponseEntity.ok(chatService.chatRoomList(memberId)); // 모든 채팅방 리스트를 응답으로 반환
    }

    // HTTP GET 요청을 통해 특정 채팅방의 정보를 조회하는 메서드
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResDto> findRoomById(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.findRoomById(roomId)); // 특정 채팅방의 정보를 응답으로 반환
    }

    // 전 메세지 가져오기(Dto 변환예정)
    @PostMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageDto>> findChatting(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getRecentMessages(roomId)); // 특정 채팅방의 정보를 응답으로 반환
    }

    @PostMapping("/updateMessageStatus")
    public ResponseEntity<String> updateMessageStatus(@RequestParam String roomId, @RequestParam Long memberId, @RequestParam String status) {
        try {
            chatService.updateMessageStatus(roomId, memberId, status);
            return ResponseEntity.ok("메시지 읽음 상태 업데이트 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메시지 읽음 상태 업데이트 실패");
        }

    }
    @GetMapping("/unreadMessages/{roomId}/{memberId}")
    public ResponseEntity<Integer> getUnreadMessageCount(@PathVariable String roomId, @PathVariable Long memberId) {
        int unreadMessageCount = chatService.getUnreadMessageCount(roomId, memberId);
        return ResponseEntity.ok(unreadMessageCount);
    }

    @GetMapping("/latestMessage/{roomId}")
    public ResponseEntity<ChatMessageDto> getLatestMessage(@PathVariable String roomId) {
        ChatMessageDto latestMessage = chatService.getLatestMessage(roomId);

        // 최신 메시지가 존재하는 경우에만 반환
        if (latestMessage != null) {
            return ResponseEntity.ok(latestMessage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}