package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.CommunityDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.entity.*;
import com.HealthCare.HealthyLife_Backend.repository.*;
import com.HealthCare.HealthyLife_Backend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            Member member = memberRepository.findByEmail(communityDto.getEmail()).orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            Category category = categoryRepository.findById(communityDto.getCategoryId()).orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );
            Community community = Community.builder()
                    .title(communityDto.getTitle())
                    .content(communityDto.getContent())
                    .text(communityDto.getText())
                    .member(member)
                    .category(category)
                    .likeCount(communityDto.getLikeCount())
                    .viewCount(communityDto.getViewCount())
                    .comments(new ArrayList<>())
                    .build();
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
            communityDtos.add(convertEntityToDto(community));
        }
        return communityDtos;
    }

    // 게시글 상세 조회
    public CommunityDto getCommunityDetail(Long id) {
        Community community = communityRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 게시물이 존재하지 않습니다.")
        );
        return convertEntityToDto(community);
    }

    // 게시글 수정
    public boolean modifyCommunity(Long id, CommunityDto communityDto) {
        try {
            Community community = communityRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
            );
            Community modifiedCommunity = Community.builder()
                    .communityId(id) // 기존의 ID를 유지
                    .title(communityDto.getTitle())
                    .content(communityDto.getContent())
                    .text(communityDto.getText())
                    .regDate(community.getRegDate()) // 등록일은 그대로 유지
                    .member(community.getMember()) // Member도 그대로 유지
                    .communityLikeIts(community.getCommunityLikeIts()) // CommunityLikeIts도 그대로 유지
                    .category(community.getCategory()) // Category도 그대로 유지
                    .likeCount(community.getLikeCount()) // likeCount도 그대로 유지
                    .viewCount(community.getViewCount()) // viewCount도 그대로 유지
                    .comments(community.getComments()) // Comments도 그대로 유지
                    .nickName(community.getNickName()) // NickName도 그대로 유지
                    .build();
            communityRepository.save(modifiedCommunity);
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
            communityDtos.add(convertEntityToDto(community));
        }
        return communityDtos;
    }

    // 게시글 페이징
    public List<CommunityDto> getCommunityList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Community> communities = communityRepository.findAll(pageable).getContent();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(convertEntityToDto(community));
        }
        return communityDtos;
    }

    // 카테고리별 게시글 페이징
    public List<CommunityDto> getCommunityListByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Community> communities = communityRepository.findByCategory_CategoryId(categoryId, pageable).getContent();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(convertEntityToDto(community));
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
        return communities.map(this::convertEntityToDto);
    }

    public Page<CommunityDto> searchByTitle(String keyword, Pageable pageable) {
        Page<Community> communities = communityRepository.findByTitleContaining(keyword, pageable);
        return communities.map(this::convertEntityToDto);
    }

    public Page<CommunityDto> searchByNickName(String keyword, Pageable pageable) {
        Page<Community> communities = communityRepository.findByNickNameContaining(keyword, pageable);
        return communities.map(this::convertEntityToDto);
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
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        // DTO 리스트를 페이지로 변환하여 반환
        // 총 요소의 수를 적절하게 계산하여 전달해야 함
        return new PageImpl<>(communityDtos, pageable, communityDtos.size());
    }

    // 좋아요
    public void like(Long communityId, String email, boolean isLikeIt) {
        Optional<Community> communityOptional = communityRepository.findById(communityId);
        if (!communityOptional.isPresent()) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        Community community = communityOptional.get();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 존재하지 않습니다."));

        Optional<CommunityLikeIt> likeItOptional = likeItRepository.findByCommunityAndMember(community, member);
        if (likeItOptional.isPresent()) {
            throw new IllegalArgumentException("이미 좋아합니다.");

        }
// 추천수 증가 감소
        int newLikeCount = isLikeIt ? community.getLikeCount() + 1 : community.getLikeCount() - 1;

        Community updatedCommunity = Community.builder()
                .communityId(community.getCommunityId())
                .title(community.getTitle())
                .content(community.getContent())
                .text(community.getText())
                .regDate(community.getRegDate())
                .member(community.getMember())
                .communityLikeIts(community.getCommunityLikeIts())
                .category(community.getCategory())
                .likeCount(newLikeCount)
                .viewCount(community.getViewCount())
                .comments(community.getComments())
                .build();

        communityRepository.save(updatedCommunity);

        CommunityLikeIt like = CommunityLikeIt.builder()
                .community(updatedCommunity)
                .isLikeIt(isLikeIt)
                .member(member)
                .build();

        likeItRepository.save(like);
    }


    // 게시글 엔티티를 DTO로 변환
    private CommunityDto convertEntityToDto(Community community) {
        return CommunityDto.builder()
                .communityId(community.getCommunityId())
                .categoryId(community.getCategory().getCategoryId())
                .memberId(community.getMember().getId())
                .title(community.getTitle())
                .content(community.getContent())
                .text(community.getText())
                .regDate(community.getRegDate())
                .likeCount(community.getLikeCount())
                .viewCount(community.getViewCount())
                .categoryName(community.getCategory().getCategoryName())
                .email(community.getMember().getEmail())
                .nickName(community.getMember().getNickName())
                .build();
    }
}



