package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByTitleContaining(String keyword);

    Page<Community> findAll(Pageable pageable);

    Page<Community> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    List<Community> findByRegDateAfter(LocalDateTime regDate);

    // 제목+내용으로 검색
    Page<Community> findByTitleContainingOrTextContaining(String title, String content, Pageable pageable);

    // 제목으로만 검색
    Page<Community> findByTitleContaining(String title, Pageable pageable);

    // 글쓴이로 검색
    Page<Community> findByNickNameContaining(String keyword, Pageable pageable);
}

