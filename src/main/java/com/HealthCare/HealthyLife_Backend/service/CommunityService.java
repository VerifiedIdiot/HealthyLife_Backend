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

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final ViewRepository viewRepository;
    private final PickRepository pickRepository;

    //        게시글 작성
    public boolean saveCommunity(CommunityDto communityDto, HttpServletRequest request) {
        try {
            Community community = new Community();
//            isEmpty:문자열의 길이가 0인 경우에, true를 리턴
            if (communityDto.getEmail() != null && !communityDto.getEmail().isEmpty()) {
                Member member = memberRepository.findByEmail(communityDto.getEmail()).orElseThrow(
                        () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
                );
                community.setMember(member);
                community.setEmail(member.getEmail());
            } else {
                String clientIp = request.getHeader("X-Forwarded-For");

                if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
                    clientIp = request.getRemoteAddr();
                }
                community.setIpAddress(clientIp);
                community.setName(communityDto.getName());
                community.setPassword(communityDto.getPassword());
            }
            CommunityCategory category = categoryRepository.findById(communityDto.getCategoryId()).orElseThrow(
                    () -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );

            community.setTitle(communityDto.getTitle());
            community.setCategory(category);
            community.setCategoryName(category.getCategoryName());
            community.setContent(communityDto.getContent());
            community.setText(communityDto.getText());

            communityRepository.save(community);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CommunityDto> getCommunityList() {
        List<Community> communities = communityRepository.findAll();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for (Community community : communities) {
            communityDtos.add(convertEntityToDto(community));
        }
        return communityDtos;
    }

    public CommunityDto getCommunityDetail(Long id, HttpServletRequest request) {
        Community community = communityRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 게시물이 존재하지 않습니다.")
        );
        String visitorIp = request.getHeader("X-Forwarded-For");
        if (visitorIp == null || visitorIp.isEmpty() || "unknown".equalsIgnoreCase(visitorIp)) {
            visitorIp = request.getRemoteAddr();
        }
        final String finalVisitorIp = visitorIp;
        List<CommunityView> CommunityViews = viewRepository.findByCommunity(community);
        if (CommunityViews.stream().noneMatch(view -> view.getIp().equals(finalVisitorIp))) {
            community.setViewCount(community.getViewCount() + 1);

            CommunityView communityViews = new CommunityView();
            communityViews.setCommunity(community);
            communityViews.setIp(finalVisitorIp);
            viewRepository.save(communityViews);
        }

        // Community 엔티티를 DTO로 변환
        CommunityDto communityDto = convertEntityToDto(community);

        communityRepository.save(community);

        return communityDto;
    }

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

    //  좋아요
    public void pick(Long communityId, String email, String visitorIp, boolean isPick) {
        Optional<Community> communityOptional = communityRepository.findById(communityId);
        if (!communityOptional.isPresent()) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        Community community = communityOptional.get();

        // 로그인한 경우 이메일로 비로그인일 경우 IP로 중복 추천 체크
        Optional<CommunityPick> pickOptional;
        if (email != null && !email.isEmpty()) {
            pickOptional = pickRepository.findByCommunityAndEmail(community, email);
        } else {
            pickOptional = pickRepository.findByCommunityAndIp(community, visitorIp);
        }

        // 이미 추천했는지 확인
        if (pickOptional.isPresent()) {
            throw new IllegalArgumentException("이미 추천하셨습니다.");
        }

        // 좋아요 수 증가 또는 감소
        if (isPick) {
            community.setPickCount(community.getPickCount() + 1);
        } else {
            community.setPickCount(community.getPickCount() - 1);
        }
        communityRepository.save(community);

        // 좋아요 기록 저장
        CommunityPick pick = new CommunityPick();
        pick.setCommunity(community);
        pick.setIp(visitorIp);
        pick.setPick(isPick);
        pickRepository.save(pick);
    }

    // 조회수, 추천 수, 댓글 수를 고려한 복합 점수 계산
    private float calculateScore(Community post) {
        int commentCount = commentRepository.countByCommunity(post);  // 댓글 수를 구함
        return post.getViewCount() * 0.3f + post.getPickCount() * 0.5f + commentCount * 0.2f;
    }

    // 실시간 랭킹
    public List<Community> getRealtimeRanking(String period) {
        // 최근 시간을 설정하고 가져옴
        LocalDateTime targetTime;

        switch (period) {
            case "realtime":
                targetTime = LocalDateTime.now().minusHours(1);
                break;
            case "weekly":
                targetTime = LocalDateTime.now().minusWeeks(1);
                break;
            case "monthly":
                targetTime = LocalDateTime.now().minusMonths(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        // 게시글을 불러옴
        List<Community> posts = communityRepository.findByRegDateAfter(targetTime);


        // 점수를 계산하여 랭킹을 매김
        posts.sort((post1, post2) -> Float.compare(calculateScore(post2), calculateScore(post1)));

        log.warn(posts.toString());

        // 상위 10개 게시글만 반환함
        return posts.subList(0, Math.min(10, posts.size()));
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

    public Page<CommunityDto> searchByName(String keyword, Pageable pageable) {
        Page<Community> communities = communityRepository.findByNameContaining(keyword, pageable);
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

    // 게시글 엔티티를 DTO로 변환
    private CommunityDto convertEntityToDto(Community community) {
        CommunityDto communityDto = new CommunityDto();
        communityDto.setId(community.getCommunityId());
        communityDto.setTitle(community.getTitle());
        communityDto.setContent(community.getContent());
        communityDto.setText(community.getText());
        communityDto.setIpAddress(community.getIpAddress());
        communityDto.setEmail(community.getEmail());
        communityDto.setName(community.getName());
        communityDto.setPassword(community.getPassword());
        communityDto.setViewCount(community.getViewCount());
        communityDto.setPickCount(community.getPickCount());
        communityDto.setCategoryId(community.getCategory().getCategoryId());
        communityDto.setCategoryName(community.getCategoryName());

        if (community.getMember() != null) {
            communityDto.setEmail(community.getMember().getEmail());
        }
        communityDto.setRegDate(community.getRegDate());

        return communityDto;
    }
}

