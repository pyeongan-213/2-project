package kr.co.duck.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.duck.domain.Music;

public interface MusicRepository extends JpaRepository<Music, Integer> {
   
	List<Music> findAll();
}
