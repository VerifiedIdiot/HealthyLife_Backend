package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.entity.CommunityPick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PickRepository extends JpaRepository<CommunityPick, Long> {
    Optional<CommunityPick> findByCommunityAndIp(Community community, String ip);

    Optional<CommunityPick> findByCommunityAndEmail(Community community, String email);

}
