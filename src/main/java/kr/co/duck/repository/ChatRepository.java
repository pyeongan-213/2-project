package kr.co.duck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.duck.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    // 특정 플레이어가 특정 방에서 마지막으로 입력한 명령어를 가져오는 쿼리
    @Query("SELECT c.chat_Text FROM Chat c WHERE c.room_Id = :roomId AND c.member_Id = :memberId ORDER BY c.chat_Time DESC")
    String findLastCommandByPlayerIdAndRoomId(@Param("memberId") int memberId, @Param("roomId") int roomId);
}
