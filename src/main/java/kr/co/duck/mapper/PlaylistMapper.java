package kr.co.duck.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface PlaylistMapper {

    // 재생목록에 음악 추가
    @Insert("INSERT INTO Playlist (musicId, playOrder) VALUES (#{musicId}, #{playOrder})")
    void insertMusicToPlaylist(int musicId, int playOrder);

    // 재생목록 조회 (순서대로)
    @Select("SELECT musicId FROM Playlist ORDER BY playOrder ASC")
    List<Integer> getPlaylistMusicIds();

    // 특정 곡을 재생목록에서 삭제
    @Delete("DELETE FROM Playlist WHERE musicId = #{musicId}")
    void deleteMusicFromPlaylist(int musicId);
}
