package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.entity.CommunityLikeIt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeItRepository extends JpaRepository<CommunityLikeIt, Long> {

    Optional<CommunityLikeIt> findByCommunityAndEmail(Community community, String email);

}
