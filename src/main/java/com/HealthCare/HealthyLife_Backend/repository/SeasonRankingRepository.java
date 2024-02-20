package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeasonRankingRepository extends JpaRepository<SeasonRanking, Long> {
    Optional<SeasonRanking> findByRegDateAndMemberEmail(String regDate, String email);

//    List<SeasonRanking> findByRegDateStartingWithOrderByPointsDesc(String yearMonth);

    // 해당 년월에 해당하는 데이터를 포인트 내림차순으로 가져오는 쿼리
    @Query("SELECT s FROM SeasonRanking s WHERE s.regDate LIKE CONCAT(:yearMonth, '%') ORDER BY s.points DESC")
    List<SeasonRanking> findByRegDateStartingWithOrderByPointsDesc(@Param("yearMonth") String yearMonth);


    Optional<SeasonRanking> findByMemberId(Long memberId);
}
