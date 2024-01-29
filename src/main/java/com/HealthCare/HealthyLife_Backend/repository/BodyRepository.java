package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Body;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodyRepository extends JpaRepository<Body, Long> {
    List<Body> findByMemberEmail(String Email);
}
