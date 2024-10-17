package kr.co.duck.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.duck.domain.Music;
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizMusicId;

@Repository
public interface PlaylistRepository extends JpaRepository<QuizMusic, QuizMusicId> {

    // 플레이리스트와 Music 엔티티를 join 해서 플레이리스트의 곡 정보를 가져옴
    @Query("SELECT m FROM PlaylistMusic pm " +
           "JOIN Music m ON pm.musicid = m.musicId " +
           "WHERE pm.playlistid = :playlistId " +
           "ORDER BY pm.playorder")
    List<Music> findMusicInPlaylist(@Param("playlistId") int playlistId);
}