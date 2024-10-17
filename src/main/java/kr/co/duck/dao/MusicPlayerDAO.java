package kr.co.duck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.duck.domain.Music;

@Mapper
public interface MusicPlayerDAO {

    // 특정 플레이리스트에서 재생 순서에 해당하는 곡을 가져오는 메서드
    @Select("SELECT m.MUSIC_ID, m.MUSIC_NAME, m.ARTIST, m.VIDEOURL, pm.PLAYORDER " +
            "FROM playlist_music pm " +
            "JOIN music m ON pm.MUSIC_ID = m.MUSIC_ID " +
            "WHERE pm.PLAYLIST_ID = #{playlistId} " +
            "AND pm.PLAYORDER = #{playOrder}")
    Music getMusicByPlayOrder(@Param("playlistId") int playlistId, 
                              @Param("playOrder") int playOrder);

    // 특정 플레이리스트의 모든 곡을 순서대로 가져오는 메서드
    @Select("SELECT m.MUSIC_ID, m.MUSIC_NAME, m.ARTIST, m.VIDEOURL, pm.PLAYORDER " +
            "FROM playlist_music pm " +
            "JOIN music m ON pm.MUSIC_ID = m.MUSIC_ID " +
            "WHERE pm.PLAYLIST_ID = #{playlistId} " +
            "ORDER BY pm.PLAYORDER")
    List<Music> getAllMusicInPlaylist(@Param("playlistId") int playlistId);
}
