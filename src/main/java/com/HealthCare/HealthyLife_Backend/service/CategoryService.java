package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.CommunityCategoryDto;
import com.HealthCare.HealthyLife_Backend.entity.CommunityCategory;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.CategoryRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    // 카테고리 등록
    public boolean saveCategory(CommunityCategoryDto communityCategoryDto) {
        try {
            CommunityCategory communityCategory = new CommunityCategory();
            Member member = memberRepository.findByEmail(communityCategoryDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재 하지 않습니다.")
            );
            communityCategory.setCategoryName(communityCategoryDto.getCategoryName());
            communityCategory.setMember(member);
            categoryRepository.save(communityCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 카테고리 수정
    public boolean modifyCategory(Long id, CommunityCategoryDto communityCategoryDto) {
        try {
            CommunityCategory communityCategory = categoryRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );
            Member member = memberRepository.findByEmail(communityCategoryDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다")
            );
            communityCategory.setCategoryName(communityCategoryDto.getCategoryName());
            communityCategory.setCategoryId(communityCategoryDto.getCategoryId());
            communityCategory.setMember(member);
            categoryRepository.save(communityCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 카테고리 삭제
    public boolean deleteCategory(Long id) {
        try {
            CommunityCategory communityCategory = categoryRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );
            categoryRepository.delete(communityCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 카테고리 목록 조회
    public List<CommunityCategoryDto> getCategoryList() {
        List<CommunityCategory> categories = categoryRepository.findAll();
        List<CommunityCategoryDto> communityCategoryDtos = new ArrayList<>();
        for (CommunityCategory communityCategory : categories) {
            communityCategoryDtos.add(convertEntityToDto(communityCategory));
        }
        return communityCategoryDtos;
    }

    // 엔티티를 DTO로 변환하는 메서드
    private CommunityCategoryDto convertEntityToDto(CommunityCategory communityCategory) {
        CommunityCategoryDto communityCategoryDto = new CommunityCategoryDto();
        communityCategoryDto.setCategoryId(communityCategory.getCategoryId());
        communityCategoryDto.setCategoryName(communityCategory.getCategoryName());
        communityCategoryDto.setEmail(communityCategory.getMember().getEmail());
        return communityCategoryDto;
    }
}

