package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.TotalRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TotalRankingRepository extends JpaRepository<TotalRanking,Long> {
    Optional<TotalRanking> findByMemberEmail(String email);
    List<TotalRanking> findTotalByOrderByPointsAsc();
}
