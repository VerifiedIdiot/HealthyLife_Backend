package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomReqDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomResDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
//@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    // HTTP POST 요청을 통해 새로운 채팅방을 생성하는 메서드
    @PostMapping("/new")
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomReqDto chatRoomDto,MemberReqDto memberReqDto,Long senderId) {
        log.warn("chatRoomDto : {}", chatRoomDto); // 전송된 ChatRoomReqDto 로그 기록
        ChatRoomResDto room = chatService.createRoom(chatRoomDto.getName(),memberReqDto.getId(),senderId); // 채팅 서비스를 통해 채팅방 생성
        System.out.println(room.getRoomId()); // 생성된 채팅방의 ID를 콘솔에 출력
        return ResponseEntity.ok(room.getRoomId()); // 생성된 채팅방의 ID를 응답으로 반환
    }
    // 메세지 저장하기
    @PostMapping("/message")
    public ResponseEntity<Void> saveMessage(@RequestBody ChatMessageDto chatMessageDTO) {
        chatService.saveMessage(chatMessageDTO.getRoomId(), chatMessageDTO.getSender(), chatMessageDTO.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // memberId로 채팅방 목록 가져오기
    @GetMapping("/rooms/owner/{memberId}")
    public ResponseEntity<List<ChatRoomResDto>> getRoomsByOwnerId(@PathVariable Long memberId) {
        List<ChatRoomResDto> chatRooms = chatService.findRoomForMember(memberId);
        return ResponseEntity.ok(chatRooms);
    }

    // 방 정보 가져오기
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResDto> findRoomById(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.findRoomById(roomId));
    }

    // 이전 메세지 가져오기
    @GetMapping("/message/{roomId}")
    public List<Chatting> getRecentMessages(@PathVariable String roomId) {
        return chatService.getRecentMessages(roomId);
    }

}