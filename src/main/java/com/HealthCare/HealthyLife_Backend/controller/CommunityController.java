package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.CommunityDto;
import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    // 게시글 작성
    @PostMapping("/new")
    public ResponseEntity<Boolean> saveCommunity(@RequestBody CommunityDto communityDto, HttpServletRequest request) {
        return ResponseEntity.ok(communityService.saveCommunity(communityDto, request));
    }

    // 게시글 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<CommunityDto>> getCommunityList() {
        return ResponseEntity.ok(communityService.getCommunityList());
    }

    // 게시글 방 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<CommunityDto> getCommunityDetail(@PathVariable Long id, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(communityService.getCommunityDetail(id, request));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> modifyCommunity(@PathVariable Long id, @RequestBody CommunityDto communityDto) {
        return ResponseEntity.ok(communityService.modifyCommunity(id, communityDto));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCommunity(@PathVariable Long id) {
        return ResponseEntity.ok(communityService.deleteCommunity(id));
    }

    // 게시글 목록 페이징
    @GetMapping("/list/page")
    public ResponseEntity<List<CommunityDto>> boardList(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        List<CommunityDto> list = communityService.getCommunityList(page, size);
        return ResponseEntity.ok(list);
    }

    // 카테고리별 게시글 목록 페이징
    @GetMapping("/list/page/category")
    public ResponseEntity<List<CommunityDto>> boardListByCategory(@RequestParam Long categoryId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        List<CommunityDto> list = communityService.getCommunityListByCategory(categoryId, page, size);
        return ResponseEntity.ok(list);
    }

    // 페이지 수 조회
    @GetMapping("/count")
    public ResponseEntity<Integer> listBoards(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Integer pageCnt = communityService.getCommunity(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    // 카테고리별 페이지 수 조회
    @GetMapping("/count/{categoryId}")
    public ResponseEntity<Integer> getCommunityTotalPagesByCategory(@PathVariable Long categoryId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Integer pageCnt = communityService.getCommunityTotalPagesByCategory(categoryId, pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    // 좋아요/싫어요
    @PostMapping("/pick/{id}/{isPick}")
    public ResponseEntity<String> pick(@PathVariable Long id, @PathVariable boolean isPick, HttpServletRequest request, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        String visitorIp = request.getRemoteAddr();
        try {
            communityService.pick(id, email, visitorIp, isPick);
            return ResponseEntity.ok(isPick ? "좋아요" : "싫어요");
        } catch (IllegalArgumentException e) {
            String message = isPick ? "이미 좋아합니다." : "이미 싫어합니다.";
            return ResponseEntity.badRequest().body(message);
        }
    }

    // 좋아요 랭킹 목록
    @GetMapping("/ranking/{period}")
    public ResponseEntity<List<Community>> getRealtimeRanking(@PathVariable String period) {
        return ResponseEntity.ok(communityService.getRealtimeRanking(period));
    }

    // 게시글 검색 기본(제목+내용)
    @GetMapping("/search/titleAndContent")
    public ResponseEntity<Page<CommunityDto>> searchByTitleAndContent(@RequestParam String keyword, Pageable pageable) {
        Page<CommunityDto> list = communityService.searchByTitleAndText(keyword, pageable);
        return ResponseEntity.ok(list);
    }

    // 제목으로 검색
    @GetMapping("/search/title")
    public ResponseEntity<Page<CommunityDto>> searchByTitle(@RequestParam String keyword, Pageable pageable) {
        Page<CommunityDto> list = communityService.searchByTitle(keyword, pageable);
        return ResponseEntity.ok(list);
    }

    // 이름으로 검색
    @GetMapping("/search/name")
    public ResponseEntity<Page<CommunityDto>> searchByName(@RequestParam String keyword, Pageable pageable) {
        Page<CommunityDto> list = communityService.searchByName(keyword, pageable);
        return ResponseEntity.ok(list);
    }

    // 댓글로 검색
    @GetMapping("/search/comment")
    public ResponseEntity<Page<CommunityDto>> Search(@RequestParam String keyword, Pageable pageable) {
        Page<CommunityDto> list = communityService.searchByComment(keyword, pageable);
        return ResponseEntity.ok(list);
    }

}