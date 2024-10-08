package kr.co.duck.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import kr.co.duck.beans.MusicBean;

@Mapper
public interface MusicDAO {

	  // 음악 추가
    @Insert("INSERT INTO MUSIC (music_id, music_name, artist, video_url, thumbnail_url) "
          + "VALUES (music_seq.NEXTVAL, #{musicName}, #{artist}, #{videoUrl}, #{thumbnailUrl})")
    void insertMusic(MusicBean music);

    // 모든 음악 조회 (SELECT)
    @Select("SELECT music_id, music_name, artist, video_url, thumbnail_url FROM MUSIC")
    List<MusicBean> getAllMusic();

    // 특정 음악 조회 (SELECT)
    @Select("SELECT music_id, music_name, artist, video_url, thumbnail_url FROM MUSIC WHERE music_id = #{musicId}")
    MusicBean getMusicById(int musicId);

    // 음악 삭제 (DELETE)
    @Delete("DELETE FROM MUSIC WHERE music_id = #{musicId}")
    void deleteMusic(int musicId);
}
