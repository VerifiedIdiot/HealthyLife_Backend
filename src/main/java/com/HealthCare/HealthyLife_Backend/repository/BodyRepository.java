package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Body;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BodyRepository extends JpaRepository<Body, Long> {
    List<Body> findByMemberEmail(String Email);

    Optional<Body> findTopByMemberEmailOrderByDateDesc(String email);

    Optional<Body> findTopByMemberEmailAndDateBeforeOrderByDateDesc(String email, LocalDate date);
}
