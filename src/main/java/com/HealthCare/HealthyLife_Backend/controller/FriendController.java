package com.HealthCare.HealthyLife_Backend.controller;


import com.HealthCare.HealthyLife_Backend.dto.FriendDto;
import com.HealthCare.HealthyLife_Backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;


@Slf4j
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구/차단 목록 출력
    @GetMapping("/list/{memberId}/{isFriend}")
    public ResponseEntity<List<FriendDto>> getFriendList(
            @PathVariable Long memberId,
            @PathVariable boolean isFriend
    ) {
        List<FriendDto> friends = friendService.getFriendList(memberId, isFriend);
        return ResponseEntity.ok(friends);
    }

    // 친구 추가
    @PostMapping("/add/friend")
    public ResponseEntity<Boolean> addFriend(@RequestBody FriendDto friendDto) {
        boolean isSuccess = friendService.saveFriend(friendDto, true);
        return ResponseEntity.ok(isSuccess);
    }

    // 차단 추가
    @PostMapping("/add/block")
    public ResponseEntity<Boolean> addBlock(@RequestBody FriendDto friendDto) {
        boolean isSuccess = friendService.saveFriend(friendDto, false);
        return ResponseEntity.ok(isSuccess);
    }

    // 통합 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteFriend(@PathVariable Long id) {
        boolean isSuccess = friendService.deleteFriend(id);
        return ResponseEntity.ok(isSuccess);
    }
}
