package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Comment;
import com.HealthCare.HealthyLife_Backend.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByContentContaining(String keyword, Pageable pageable);

    List<Comment> findByContentContaining(String keyword);

    List<Comment> findByCommunity(Community community);

    Page<Comment> findByCommunity(Community community, Pageable pageable);

    int countByCommunity(Community community);
}