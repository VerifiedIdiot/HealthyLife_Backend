package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.CategoryDto;
import com.HealthCare.HealthyLife_Backend.entity.Category;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.CategoryRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;


    // 카테고리 등록
    public boolean saveCategory(CategoryDto categoryDto) {
        try {
            Member member = memberRepository.findByEmail(categoryDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재 하지 않습니다.")
            );
            Category category = Category.builder()
                    .categoryName(categoryDto.getCategoryName())
                    .member(member)
                    .build();
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 카테고리 수정
    public boolean modifyCategory(Long id, CategoryDto categoryDto) {
        try {
            Member member = memberRepository.findByEmail(categoryDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다")
            );
            Category category = categoryRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );

            category = Category.builder()
                    .categoryName(categoryDto.getCategoryName())
                    .member(member)
                    .build();
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 카테고리 삭제
    public boolean deleteCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")
            );
            categoryRepository.delete(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 카테고리 목록 조회
    public List<CategoryDto> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(convertEntityToDto(category));
        }
        return categoryDtos;
    }

    // 엔티티를 DTO로 변환하는 메서드
    private CategoryDto convertEntityToDto(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .email(category.getMember().getEmail())
                .build();
    }
}
