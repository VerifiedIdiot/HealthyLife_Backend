package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.ChatRoomReqDto;
import com.HealthCare.HealthyLife_Backend.dto.ChatRoomResDto;
import com.HealthCare.HealthyLife_Backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomReqDto chatRoomDto) {
        log.warn("chatRoomDto : {}", chatRoomDto); // 전송된 ChatRoomReqDto 로그 기록
        ChatRoomResDto room = chatService.createRoom(chatRoomDto.getName()); // 채팅 서비스를 통해 채팅방 생성
        System.out.println(room.getRoomId()); // 생성된 채팅방의 ID를 콘솔에 출력
        return ResponseEntity.ok(room.getRoomId()); // 생성된 채팅방의 ID를 응답으로 반환
    }

    // HTTP GET 요청을 통해 모든 채팅방 리스트를 조회하는 메서드
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResDto>> findAllRoom() {
        return ResponseEntity.ok(chatService.findAllRoom()); // 모든 채팅방 리스트를 응답으로 반환
    }

    // HTTP GET 요청을 통해 특정 채팅방의 정보를 조회하는 메서드
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResDto> findRoomById(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.findRoomById(roomId)); // 특정 채팅방의 정보를 응답으로 반환
    }
}