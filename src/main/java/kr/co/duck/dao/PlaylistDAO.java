package kr.co.duck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.beans.PlaylistBean;

@Mapper
public interface PlaylistDAO {

    // 플레이리스트 생성
    @Insert("INSERT INTO PLAYLIST (PLAYLIST_ID, PLAYLISTNAME, MEMBER_ID, CREATEDDATE) "
            + "VALUES (playlist_seq.NEXTVAL, #{playlistName}, #{memberId}, SYSDATE)")
    void insertPlaylist(PlaylistBean playlist);

    // member_id로 플레이리스트 조회
    @Select("SELECT PLAYLIST_ID, PLAYLISTNAME FROM PLAYLIST WHERE MEMBER_ID = #{memberId}")
    List<PlaylistBean> getPlaylistsByMemberId(int memberId);

    // 모든 플레이리스트 조회
    @Select("SELECT PLAYLIST_ID, PLAYLISTNAME FROM PLAYLIST")
    List<PlaylistBean> getAllPlaylists();

    // 특정 플레이리스트의 음악 목록 조회
    @Select("SELECT m.MUSIC_ID, m.MUSIC_NAME, m.ARTIST, m.VIDEOURL, m.THUMBNAILURL "
            + "FROM MUSIC m JOIN PLAYLIST_MUSIC pm ON m.MUSIC_ID = pm.MUSIC_ID "
            + "WHERE pm.PLAYLIST_ID = #{playlistId} ORDER BY pm.PLAYORDER")
    List<MusicBean> getMusicListByPlaylistId(int playlistId);
    
    // 플레이리스트에 음악 추가
    @Insert("INSERT INTO PLAYLIST_MUSIC (PLAYLIST_MUSIC_ID, PLAYLIST_ID, MUSIC_ID, MEMBER_ID, PLAYORDER) "
            + "VALUES (playlist_music_seq.NEXTVAL, #{playlistId}, #{musicId}, #{memberId}, "
            + "(SELECT COALESCE(MAX(PLAYORDER), 0) + 1 FROM PLAYLIST_MUSIC WHERE PLAYLIST_ID = #{playlistId}))")
    void addMusicToPlaylist(@Param("playlistId") int playlistId, @Param("musicId") int musicId, 
                            @Param("memberId") int memberId);

    // 플레이리스트에서 음악 삭제
    @Delete("DELETE FROM PLAYLIST_MUSIC WHERE PLAYLIST_ID = #{playlistId} AND MUSIC_ID = #{musicId}")
    void removeMusicFromPlaylist(@Param("playlistId") int playlistId, @Param("musicId") int musicId);

    // 플레이리스트의 음악 순서 변경
    @Update("UPDATE PLAYLIST_MUSIC SET PLAYORDER = #{playOrder} "
            + "WHERE PLAYLIST_ID = #{playlistId} AND MUSIC_ID = #{musicId}")
    void updateMusicOrder(@Param("playlistId") int playlistId, @Param("musicId") int musicId,
                          @Param("playOrder") int playOrder);

    // 플레이리스트 삭제
    @Delete("DELETE FROM PLAYLIST WHERE PLAYLIST_ID = #{playlistId}")
    void deletePlaylist(int playlistId);
}
