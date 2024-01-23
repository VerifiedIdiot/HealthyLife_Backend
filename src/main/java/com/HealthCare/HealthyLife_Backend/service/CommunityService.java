package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.CommunityDto;
import com.HealthCare.HealthyLife_Backend.entity.*;
import com.HealthCare.HealthyLife_Backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.HealthCare.HealthyLife_Backend.security.SecurityUtil.getCurrentMemberId;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final LikeItRepository likeItRepository;

    //        게시글 작성
    public boolean saveCommunity(CommunityDto communityDto) {
        try {
            Community community = new Community();
            Long memberId = getCurrentMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            Category category = categoryRepository.findById(communityDto.getCommunityId()).orElseThrow(
                    () -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );
            community.setTitle(communityDto.getTitle());
            community.setCategory(category);
            community.setContent(communityDto.getContent());
            community.setText(communityDto.getText());
            community.setMember(member);
            communityRepository.save(community);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 게시글 전체 조회
    public List<CommunityDto> getCommunityList() {
        List<Community> communities = communityRepository.findAll();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(toCommunityEntity(community));
        }
        return communityDtos;
    }

    // 게시글 상세 조회
    public CommunityDto getCommunityDetail(Long id, HttpServletRequest request) {
        Community community = communityRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 게시물이 존재하지 않습니다.")
        );
        return toCommunityEntity(community);
    }

    // 게시글 수정
    public boolean modifyCommunity(Long id, CommunityDto communityDto) {
        try {
            Community community = communityRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
            );
            community.setTitle(communityDto.getTitle());
            community.setContent(communityDto.getContent());
            community.setText(communityDto.getText());
            communityRepository.save(community);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 게시글 삭제
    public boolean deleteCommunity(Long id) {
        try {
            communityRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 게시글 검색
    public List<CommunityDto> searchCommunity(String keyword) {
        List<Community> communities = communityRepository.findByTitleContaining(keyword);
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(toCommunityEntity(community));
        }
        return communityDtos;
    }

    // 게시글 페이징
    public List<CommunityDto> getCommunityList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Community> communities = communityRepository.findAll(pageable).getContent();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(toCommunityEntity(community));
        }
        return communityDtos;
    }

    // 카테고리별 게시글 페이징
    public List<CommunityDto> getCommunityListByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Community> communities = communityRepository.findByCategory_CategoryId(categoryId, pageable).getContent();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(toCommunityEntity(community));
        }
        return communityDtos;
    }

    // 페이지 수 조회
    public int getCommunity(Pageable pageable) {
        return communityRepository.findAll(pageable).getTotalPages();
    }

    // 카테고리에 따른 페이지 수 조회
    public int getCommunityTotalPagesByCategory(Long categoryId, Pageable pageable) {
        return communityRepository.findByCategory_CategoryId(categoryId, pageable).getTotalPages();
    }

    // 게시글 페이지네이션 검색
    public Page<CommunityDto> searchByTitleAndText(String keyword, Pageable pageable) {
        Page<Community> communities = communityRepository.findByTitleContainingOrTextContaining(keyword, keyword, pageable);
        return communities.map(this::toCommunityEntity);
    }

    public Page<CommunityDto> searchByTitle(String keyword, Pageable pageable) {
        Page<Community> communities = communityRepository.findByTitleContaining(keyword, pageable);
        return communities.map(this::toCommunityEntity);
    }

    public Page<CommunityDto> searchByNickName(String keyword, Pageable pageable) {
        Page<Community> communities = communityRepository.findByNickNameContaining(keyword, pageable);
        return communities.map(this::toCommunityEntity);
    }

    public Page<CommunityDto> searchByComment(String keyword, Pageable pageable) {
        // 먼저 키워드를 포함하는 댓글
        Page<Comment> comments = commentRepository.findByContentContaining(keyword, pageable);

        // 찾은 댓글들이 속한 Community들을 찾기
        // 여기서 distinct()를 사용하여 중복을 제거하고, ID를 기준으로 정렬
        List<Community> uniqueCommunities = comments.getContent().stream()
                .map(Comment::getCommunity)
                .distinct()
                .sorted(Comparator.comparingLong(Community::getCommunityId))
                .collect(Collectors.toList());

        // Community들을 DTO로 변환
        // 중복 제거된 리스트를 사용
        List<CommunityDto> communityDtos = uniqueCommunities.stream()
                .map(this::toCommunityEntity)
                .collect(Collectors.toList());

        // DTO 리스트를 페이지로 변환하여 반환
        // 총 요소의 수를 적절하게 계산하여 전달해야 함
        return new PageImpl<>(communityDtos, pageable, communityDtos.size());
    }

    // 게시글 엔티티를 DTO로 변환
    private CommunityDto toCommunityEntity(Community community) {
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(community.getCommunityId());
        communityDto.setTitle(community.getTitle());
        communityDto.setContent(community.getContent());
        communityDto.setText(community.getText());
        communityDto.setRegDate(community.getRegDate());
        communityDto.setViewCount(community.getViewCount());
        communityDto.setCategoryName(community.getCategoryName());
        communityDto.setEmail(community.getMember().getEmail());
        communityDto.setNickName(community.getNickName());
        communityDto.setPassword(community.getPassword());

        return communityDto;
    }

}


