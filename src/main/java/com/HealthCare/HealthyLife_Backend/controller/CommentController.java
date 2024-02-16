package com.HealthCare.HealthyLife_Backend.controller;


import com.HealthCare.HealthyLife_Backend.dto.CommentDto;
import com.HealthCare.HealthyLife_Backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Slf4j
@CrossOrigin(origins = CORS_ORIGIN)
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> commentRegister(@RequestBody CommentDto commentDto) {
        log.info("commentDto: {}", commentDto);
        // CommentDto 객체의 communityId 필드가 널(null)인지 확인
        if (commentDto.getCommunityId() == null) {
            // 널(null)일 경우, 적절한 에러 응답 반환
            log.error("CommunityId must not be null!");
            return ResponseEntity.badRequest().body(false); // 예시: HTTP 400 Bad Request 반환
        }
        boolean result = commentService.commentRegister(commentDto);
        return ResponseEntity.ok(result);
    }

    // 댓글 수정
    @PutMapping("/modify")
    public ResponseEntity<Boolean> commentModify(@RequestBody CommentDto commentDto) {
        boolean result = commentService.commentModify(commentDto);
        return ResponseEntity.ok(result);
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Boolean> commentDelete(@PathVariable Long commentId) {
        boolean result = commentService.commentDelete(commentId);
        return ResponseEntity.ok(result);
    }

    // 댓글 목록 조회
    @GetMapping("/list/{communityId}")
    public ResponseEntity<List<CommentDto>> commentList(@PathVariable Long communityId, @RequestParam(defaultValue = "최신순") String sortType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("communityId: {}, sortType: {}, page: {}, size: {}", communityId, sortType, page, size);
        List<CommentDto> list = commentService.getCommentList(communityId, sortType, page, size);
        return ResponseEntity.ok(list);
    }

    // 댓글 목록 페이징
    @GetMapping("/list/{communityId}/page")
    public ResponseEntity<Page<CommentDto>> commentListPage(@PathVariable Long communityId,
                                                            @RequestParam(defaultValue = "최신순") String sortType, Pageable pageable) {
        Page<CommentDto> list = commentService.getCommentListPage(communityId, sortType, pageable);
        return ResponseEntity.ok(list);
    }

    // 전체 댓글 수 조회
    @GetMapping("/count/{communityId}")
    public ResponseEntity<Integer> commentCount(@PathVariable Long communityId) {
        int count = commentService.getCommentCount(communityId);
        return ResponseEntity.ok(count);
    }

    // 댓글 검색
    @GetMapping("/search")
    public ResponseEntity<List<CommentDto>> commentSearch(@RequestParam String keyword) {
        List<CommentDto> list = commentService.getCommentList(keyword);
        return ResponseEntity.ok(list);
    }
}
