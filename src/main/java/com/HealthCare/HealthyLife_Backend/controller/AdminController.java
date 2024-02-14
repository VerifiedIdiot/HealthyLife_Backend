package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.CommunityDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.service.AdminService;
import com.HealthCare.HealthyLife_Backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Slf4j
@RestController
@RequestMapping("/admin/member")
@CrossOrigin(origins = CORS_ORIGIN)
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // 회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberResDto>> memberList() {
        List<MemberResDto> list = adminService.getAdminMemberList();
        log.info("잘왔니 : " + list);
        return ResponseEntity.ok(list);
    }

    // 회원 삭제
    @DeleteMapping("/members/{email}")
    public ResponseEntity<Boolean> memberDelete(@PathVariable String email) {
        boolean isTrue = adminService.deleteMember(email);
        return ResponseEntity.ok(isTrue);
    }
    // 페이지네이션
    @GetMapping("/list/page")
    public ResponseEntity<List<MemberResDto>> memberList(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        List<MemberResDto> list = adminService.getMemberList(page, size);
        return ResponseEntity.ok(list);
    }

    // 페이지 수 조회
    @GetMapping("/list/count")
    public ResponseEntity<Integer> listMembers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = adminService.getMembers(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    @DeleteMapping("/community/delete/{id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long id){
        log.info("게시글 삭제 id : {}", id);
        return ResponseEntity.ok(adminService.deleteCommunity(id));
    }


    // 게시글 페이지네이션
    @GetMapping("/boardlist")
    public ResponseEntity<List<CommunityDto>> adminBoardList(@RequestParam (defaultValue = "0") int page,
                                                             @RequestParam (defaultValue = "10") int size){
        return ResponseEntity.ok(adminService.getAdminBoardList(page, size));
    }

    // 총 페이지 수
    @GetMapping("/totalpage")
    public ResponseEntity<Integer> adminBoardPages(@RequestParam (defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size){
        PageRequest pageRequest = PageRequest.of(page,size);
        return ResponseEntity.ok(adminService.getAdminBoardPage(pageRequest));
    }
}
