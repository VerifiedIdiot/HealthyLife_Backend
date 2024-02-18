package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRankingRepository extends JpaRepository<SeasonRanking, Long> {
    Optional<SeasonRanking> findByRegDateAndMemberEmail(String regDate, String email);
    List<SeasonRanking> findByMemberEmail(String email);
    List<SeasonRanking> findSeasonByOrderByPointsAsc();
}
