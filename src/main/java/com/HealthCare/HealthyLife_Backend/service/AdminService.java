package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.CommunityDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.CommunityRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;    // 회원
    private final CommunityRepository communityRepository;


    public List<MemberResDto> getAdminMemberList() {
        List<Member> members = memberRepository.findAll();
        List<MemberResDto> memberResDtos = new ArrayList<>();
        for (Member member : members) {
            memberResDtos.add(MemberResDto.of(member));
        }
        return memberResDtos;
    }
    // 회원 삭제
    public boolean deleteMember(String email) {
        try {
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            memberRepository.delete(member);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
    // 회원 페이지네이션
    public List<MemberResDto> getMemberList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAll(pageable);

        List<Member> members = memberPage.getContent();
        List<MemberResDto> memberResDtos = new ArrayList<>();
        for(Member member : members) {
            memberResDtos.add(MemberResDto.of(member));
        }
        return memberResDtos;
    }

    // 페이지 수 조회
    public int getMembers(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return memberPage.getTotalPages();
    }

    // 게시물 삭제
    public boolean deleteCommunity(Long id) {
        try {
            Community community = communityRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 게시글이 존재하지 않습니다.")
            );
            communityRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 게시글 페이지네이션
    public List<CommunityDto> getAdminBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        List<Community> communities = communityRepository.findAll(pageable).getContent();
        List<CommunityDto> communityDtos = new ArrayList<>();
        for(Community community : communities) {
            communityDtos.add(convertEntityToDto(community));
        }
        return communityDtos;
    }

    // 총 페이지 수
    public int getAdminBoardPage(Pageable pageable) {
        return communityRepository.findAll(pageable).getTotalPages();
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