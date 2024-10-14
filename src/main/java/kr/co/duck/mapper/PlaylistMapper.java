package kr.co.duck.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import kr.co.duck.beans.PlaylistBean;
import kr.co.duck.beans.MusicBean;

public interface PlaylistMapper {

    // 플레이리스트 생성
    @Insert("INSERT INTO PLAYLIST (PLAYLIST_ID, MEMBER_ID, PLAYLISTNAME, CREATEDDATE) " +
            "VALUES (playlist_seq.NEXTVAL, #{memberId}, #{playlistName}, SYSDATE)")
    void insertPlaylist(PlaylistBean playlist);

    // 특정 멤버의 모든 플레이리스트 조회
    @Select("SELECT * FROM PLAYLIST WHERE MEMBER_ID = #{memberId}")
    List<PlaylistBean> getPlaylistsByMemberId(int memberId);

    // 플레이리스트 삭제
    @Delete("DELETE FROM PLAYLIST WHERE PLAYLIST_ID = #{playlistId}")
    void deletePlaylist(int playlistId);

    // 플레이리스트에 음악 추가
    @Insert("INSERT INTO PLAYLIST_MUSIC (PLAYLIST_MUSIC_ID, PLAYLIST_ID, MUSIC_ID, MEMBER_ID, PLAYORDER) " +
            "VALUES (playlist_music_seq.NEXTVAL, #{playlistId}, #{musicId}, #{memberId}, #{playOrder})")
    void addMusicToPlaylist(@Param("playlistId") int playlistId, @Param("musicId") int musicId, @Param("memberId") int memberId, @Param("playOrder") int playOrder);

    // 특정 플레이리스트의 모든 음악 가져오기
    @Select("SELECT M.* FROM PLAYLIST_MUSIC PM " +
            "JOIN MUSIC M ON PM.MUSIC_ID = M.MUSIC_ID " +
            "WHERE PM.PLAYLIST_ID = #{playlistId}" +
            "order by pm.playorder")
    List<MusicBean> getMusicListByPlaylistId(int playlistId);

    // 플레이리스트에서 음악 삭제
    @Delete("DELETE FROM PLAYLIST_MUSIC WHERE PLAYLIST_ID = #{playlistId} AND MUSIC_ID = #{musicId}")
    void removeMusicFromPlaylist(@Param("playlistId") int playlistId, @Param("musicId") int musicId);

    // 음악 순서 업데이트
    @Update("UPDATE PLAYLIST_MUSIC SET PLAYORDER = #{playOrder} " +
            "WHERE PLAYLIST_ID = #{playlistId} AND MUSIC_ID = #{musicId}")
    void updateMusicOrder(@Param("playlistId") int playlistId, @Param("musicId") int musicId, @Param("playOrder") int playOrder);
}
