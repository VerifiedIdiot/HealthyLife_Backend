package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FreindRepository extends JpaRepository<Friend,Long> {
    List<Friend> findAllByMemberId(Long memberId);
}
