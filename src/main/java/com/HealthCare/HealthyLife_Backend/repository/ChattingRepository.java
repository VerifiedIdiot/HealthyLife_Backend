package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChattingRepository extends JpaRepository<Chatting,Long> {
    // order BY 로 정렬해서 채팅친 순서로 가져오기 개수는 20개 제한 이유는 채팅이 많아지면 가져오기 힘들기 떄문에
    @Query(value = "SELECT * FROM chat WHERE room_id = ?1 ORDER BY sent_at ASC LIMIT 20", nativeQuery = true)
    List<Chatting> findRecentMessages(String roomId);
}
