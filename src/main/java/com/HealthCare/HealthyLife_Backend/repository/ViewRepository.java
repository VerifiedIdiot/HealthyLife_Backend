package com.HealthCare.HealthyLife_Backend.repository;


import com.HealthCare.HealthyLife_Backend.entity.Community;
import com.HealthCare.HealthyLife_Backend.entity.CommunityView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRepository extends JpaRepository<CommunityView, Long> {
    List<CommunityView> findByCommunity(Community community);
}
