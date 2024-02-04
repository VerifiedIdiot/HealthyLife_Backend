package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.MemberStatusDto;
import com.HealthCare.HealthyLife_Backend.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Slf4j
@RestController
@RequestMapping("/status")
@CrossOrigin(origins = CORS_ORIGIN)
@RequiredArgsConstructor
public class MemberStatusControllor {
    private final MemberStatusService memberStatusService;
    // 상태 변경
    @PutMapping("/changeStatus/{memberId}/{newStatus}")
    public ResponseEntity<String> changeStatus(@PathVariable Long memberId, @PathVariable String newStatus) {
        try {
            memberStatusService.changeStatus(memberId, newStatus);
            return ResponseEntity.ok("Status changed successfully");
        } catch (EntityNotFoundException e) {
            log.error("Error changing status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK.NOT_FOUND).body("Member not found");
        } catch (Exception e) {
            log.error("Error changing status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // 최근 접속 시간 업데이트
    @PutMapping("/updateLastAccessTime/{memberId}")
    public ResponseEntity<String> updateLastAccessTime(@PathVariable Long memberId) {
        try {
            memberStatusService.updateLastAccessTime(memberId, LocalDateTime.now());
            return ResponseEntity.ok("Last access time updated successfully");
        } catch (EntityNotFoundException e) {
            log.error("Error updating last access time: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        } catch (Exception e) {
            log.error("Error updating last access time: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // 상태 메세지 변경
    @PutMapping("/updateStatusMessage/{memberId}/{newStatusMessage}")
    public ResponseEntity<String> updateStatusMessage(@PathVariable Long memberId, @PathVariable String newStatusMessage) {
        try {
            memberStatusService.updateStatusMessage(memberId, newStatusMessage);
            return ResponseEntity.ok("Status message updated successfully");
        } catch (EntityNotFoundException e) {
            log.error("Error updating status message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        } catch (Exception e) {
            log.error("Error updating status message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/getMemberStatusInfo/{memberId}")
    public ResponseEntity<MemberStatusDto> getMemberStatusInfo(@PathVariable Long memberId) {
        try {
            MemberStatusDto memberStatusDto = memberStatusService.getMemberStatusInfo(memberId);
            return ResponseEntity.ok(memberStatusDto);
        } catch (EntityNotFoundException e) {
            log.error("Error getting member status info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error getting member status info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
