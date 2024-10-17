package kr.co.duck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.duck.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
	
}