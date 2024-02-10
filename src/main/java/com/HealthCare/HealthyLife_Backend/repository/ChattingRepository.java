package com.HealthCare.HealthyLife_Backend.repository;

import com.HealthCare.HealthyLife_Backend.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChattingRepository extends JpaRepository<Chatting,Long> {
    // order BY 로 정렬해서 채팅친 순서로 가져오기 개수는 20개 제한 이유는 채팅이 많아지면 가져오기 힘들기 떄문에
    @Query(value = "SELECT * FROM chatting_tb WHERE room_id = ?1 ORDER BY message_time DESC LIMIT 20", nativeQuery = true)
    List<Chatting> findRecentMessages(String roomId);
    @Query("SELECT c FROM Chatting c WHERE c.chatRoom.roomId = :roomId AND c.member.id != :memberId AND c.messageStatus = '안읽음'")
    List<Chatting> findUnreadMessages(@Param("roomId") String roomId, @Param("memberId") Long memberId);

    @Query("SELECT c FROM Chatting c " +
            "WHERE c.chatRoom.roomId = :roomId " +
            "AND c.messageTime = (" +
            "   SELECT MAX(c2.messageTime) FROM Chatting c2 WHERE c2.chatRoom.roomId = :roomId" +
            ") " +
            "ORDER BY c.messageTime DESC")
    List<Chatting> findLatestMessages(@Param("roomId") String roomId);

    List<Chatting> findByChatRoom_RoomIdAndMemberIdOrderByMessageTimeDesc(String roomId, Long memberId);
}
