package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.ChatRoom;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    List<ChatRoom>findByMemberOrSenderId(Member member,Long senderId);
}
