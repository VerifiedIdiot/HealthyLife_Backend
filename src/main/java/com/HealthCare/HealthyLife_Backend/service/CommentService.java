package com.HealthCare.HealthyLife_Backend.service;


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

import java.util.*;

import static com.HealthCare.HealthyLife_Backend.security.SecurityUtil.getCurrentMemberId;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    // 댓글 등록
    public boolean commentRegister(CommentDto commentDto) {
        try {
            Member member = memberRepository.findByEmail(commentDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다")
            );
            Community community = communityRepository.findById(commentDto.getCommunityId()).orElseThrow(
                    () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
            );

            Comment comment = Comment.builder()
                    .content(commentDto.getContent())
                    .member(member)
                    .community(community)
                    .build();
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
            return commentDtos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Page<CommentDto> getCommentListPage(Long communityId, String sortType, Pageable pageable) {
        try {
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
            return comments.map(this::convertEntityToDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .communityId(comment.getCommunity().getCommunityId())
                .email(comment.getMember().getEmail())
                .nickName(comment.getMember().getNickName())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .build();
    }
}
