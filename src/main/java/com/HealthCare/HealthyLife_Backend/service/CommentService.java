package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.config.WebSocketHandler;
import com.HealthCare.HealthyLife_Backend.dto.CommentDto;
import com.HealthCare.HealthyLife_Backend.entity.Comment;
import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.CommentRepository;
import com.HealthCare.HealthyLife_Backend.repository.CommunityRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final ObjectMapper objectMapper;

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final WebSocketHandler webSocketHandler;

    // 댓글 등록
    public boolean commentRegister(CommentDto commentDto, HttpServletRequest request) {
        try {
            Comment comment = new Comment();
            setCommunity(comment, commentDto);
            comment.setIpAddress(getClientIP(request));
            setMemberOrAnonymous(comment, commentDto);
            setParentComment(comment, commentDto);

            comment.setContent(commentDto.getContent());
            comment.setEmail(commentDto.getEmail());
            commentRepository.save(comment);

            sendNotification(comment);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 아이피 가져오기
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // 게시글 세팅
    private void setCommunity(Comment comment, CommentDto commentDto) {
        Community community = communityRepository.findById(commentDto.getCommunityId()).orElseThrow(
                () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
        );
        comment.setCommunity(community);
    }

    private void setMemberOrAnonymous(Comment comment, CommentDto commentDto) {
        if (commentDto.getEmail() != null && !commentDto.getEmail().isEmpty()) {
            Member member = memberRepository.findByEmail(commentDto.getEmail()).orElse(null);
            if (member != null) { // 회원이 존재하는 경우
                comment.setMember(member);
                comment.setName(member.getName());
            } else {
                setAnonymous(comment, commentDto);
            }
        } else { // 이메일이 null이거나 빈 문자열인 경우
            setAnonymous(comment, commentDto);
        }
    }

    private void setAnonymous(Comment comment, CommentDto commentDto) {
        comment.setName(commentDto.getName());
        comment.setPassword(commentDto.getPassword());
    }

    private void setParentComment(Comment comment, CommentDto commentDto) {
        if (commentDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(commentDto.getParentCommentId()).orElseThrow(
                    () -> new RuntimeException("해당 부모 댓글이 존재하지 않습니다.")
            );
            comment.setParentComment(parentComment);

        }
    }

    private void sendNotification(Comment comment) throws IOException {
        Member postAuthor = comment.getCommunity().getMember();
        String postEmail = postAuthor != null ? postAuthor.getEmail() : null;
        String postIpAddress = comment.getCommunity().getIpAddress(); // 게시글 작성자의 IP 주소

        List<WebSocketSession> postAuthorSessions = webSocketHandler.getUserSessionMap().get(
                postEmail != null ? postEmail : postIpAddress // 이메일이 없는 경우 IP 주소를 사용합니다.
        );
        if (postAuthorSessions != null) {
            for (WebSocketSession postAuthorSession : postAuthorSessions) {
                if (postAuthorSession.isOpen()) { // 세션이 열려있는 경우에만 메시지를 보냅니다.
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("message", "새로운 댓글이 작성되었습니다: " + comment.getContent());
                    String messageJson = objectMapper.writeValueAsString(messageMap);
                    postAuthorSession.sendMessage(new TextMessage(messageJson)); // JSON 형식의 알림 메시지를 보냅니다.
                }
            }
        }
    }

    // 대댓글 등록
    public boolean replyRegister(CommentDto commentDto, HttpServletRequest request) {
        try {
            Comment comment = new Comment();
            setCommunity(comment, commentDto);
            comment.setIpAddress(getClientIP(request));
            setMemberOrAnonymous(comment, commentDto);
            setParentCommentForReply(comment, commentDto);

            comment.setContent(commentDto.getContent());
            commentRepository.save(comment);

            sendNotification(comment);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setParentCommentForReply(Comment comment, CommentDto commentDto) {
        if (commentDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(commentDto.getParentCommentId()).orElseThrow(
                    () -> new RuntimeException("해당 부모 댓글이 존재하지 않습니다.")
            );
            comment.setParentComment(parentComment);
        } else {
            throw new RuntimeException("대댓글을 작성하려면 부모 댓글의 ID가 필요합니다.");
        }
    }

    // 댓글 수정
    public boolean commentModify(CommentDto commentDto) {
        try {
            Comment comment = commentRepository.findById(commentDto.getCommentId()).orElseThrow(
                    () -> new RuntimeException("해당 댓글이 존재하지 않습니다.")
            );
            comment.setContent(commentDto.getContent());
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 댓글 삭제
    public boolean commentDelete(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new RuntimeException("해당 댓글이 존재하지 않습니다.")
            );
            commentRepository.delete(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 댓글 목록 조회
    public List<CommentDto> getCommentList(Long communityId, String sortType, int page, int size) {
        try {
            Community community = communityRepository.findById(communityId).orElseThrow(
                    () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
            );
            Sort sort;
            switch (sortType) {
                case "최신순":
                    sort = Sort.by(Sort.Direction.DESC, "commentId");
                    break;
                case "등록순":
                    sort = Sort.by(Sort.Direction.ASC, "commentId");
                    break;
                default:
                    sort = Sort.unsorted();
                    break;
            }
            PageRequest pageable = PageRequest.of(page, size, sort);
            List<Comment> comments = commentRepository.findByCommunity(community, pageable).getContent();
            List<CommentDto> commentDtos = new ArrayList<>();
            for (Comment comment : comments) {
                commentDtos.add(convertEntityToDto(comment));
            }
            if (sortType.equals("답글순")) {
                commentDtos.sort(Comparator.comparing(commentDto -> commentDto.getChildComments().size(), Comparator.reverseOrder()));
            }

            return commentDtos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Page<CommentDto> getCommentListPage(Long communityId, String sortType, Pageable pageable) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        Sort sort;
        switch (sortType) {
            case "최신순":
                sort = Sort.by(Sort.Direction.DESC, "commentId");
                break;
            case "등록순":
                sort = Sort.by(Sort.Direction.ASC, "commentId");
                break;
            default:
                sort = Sort.unsorted();
                break;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Comment> comments = commentRepository.findByCommunity(community, pageable);
        List<CommentDto> commentDtos = new ArrayList<>(comments.map(this::convertEntityToDto).getContent());

        if (sortType.equals("답글순")) {
            commentDtos.sort(Comparator.comparing(commentDto -> commentDto.getChildComments().size(), Comparator.reverseOrder()));
        }

        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }

    // 댓글 검색
    public List<CommentDto> getCommentList(String keyword) {
        List<Comment> comments = commentRepository.findByContentContaining(keyword);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(convertEntityToDto(comment));
        }
        return commentDtos;
    }

    // 전체 댓글 수 조회
    public int getCommentCount(Long communityId) {
        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
        );
        return commentRepository.countByCommunity(community);
    }

    // 댓글 엔티티를 DTO로 변환
    private CommentDto convertEntityToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setCommunityId(comment.getCommunity().getCommunityId());
        commentDto.setContent(comment.getContent());
        commentDto.setRegDate(comment.getRegDate());
        commentDto.setName(comment.getName());
        commentDto.setPassword(comment.getPassword());
        commentDto.setIpAddress(comment.getIpAddress());
        if (comment.getMember() != null) { // 회원이 존재하는 경우
            commentDto.setEmail(comment.getMember().getEmail());
        } else { // 회원이 존재하지 않는 경우
            commentDto.setName(comment.getName());
            commentDto.setPassword(comment.getPassword());
        }
        if (comment.getParentComment() != null) {
            commentDto.setParentCommentId(comment.getParentComment().getCommentId());
        }
        List<CommentDto> childComments = new ArrayList<>();
        for (Comment childComment : comment.getChildComments()) {
            childComments.add(convertEntityToDto(childComment));
        }
        commentDto.setChildComments(childComments);
        return commentDto;
    }
}
