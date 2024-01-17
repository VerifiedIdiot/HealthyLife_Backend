package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickName(String alias);
    boolean existsByPhone(String phone);
    boolean existsByPassword(String password);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);

}
