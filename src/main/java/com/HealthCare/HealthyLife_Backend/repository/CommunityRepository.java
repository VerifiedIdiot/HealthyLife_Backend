package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    //    제목 검색할 때 필요함
    List<Community> findByTitleContaining(String keyword);

    //    커뮤니티 페이지 네이션
    Page<Community> findAll(Pageable pageable);

    //    카테고리의 카테고리 아이디 찾아서 페이지네이션
    Page<Community> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    //    등록일 찾아줘
    List<Community> findByRegDateAfter(LocalDateTime regDate);

    //     제목 + 내용으로 검색
    Page<Community> findByTitleContainingOrTextContaining(String title, String content, Pageable pageable);

    //    제목으로만 검색
    Page<Community> findByTitleContaining(String title, Pageable pageable);

    //    이름으로만 검색
    Page<Community> findByNameContaining(String keyword, Pageable pageable);

}
