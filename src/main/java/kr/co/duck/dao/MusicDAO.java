package kr.co.duck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import kr.co.duck.beans.MusicBean;

@Mapper
public interface MusicDAO {

    // 음악 추가
    @Insert("INSERT INTO MUSIC (MUSIC_ID, MUSIC_NAME, ARTIST, VIDEOURL, THUMBNAILURL) "
            + "VALUES (music_seq.NEXTVAL, #{music_Name}, #{artist}, #{videoUrl}, #{thumbnailUrl})")
    @SelectKey(statement = "SELECT music_seq.CURRVAL FROM dual", keyProperty = "musicId", before = false, resultType = int.class)
    void insertMusic(MusicBean music);

    // 모든 음악 조회
    @Select("SELECT MUSIC_ID, MUSIC_NAME, ARTIST, VIDEOURL, THUMBNAILURL FROM MUSIC")
    List<MusicBean> getAllMusic();

    // 특정 음악 조회
    @Select("SELECT MUSIC_ID, MUSIC_NAME, ARTIST, VIDEOURL, THUMBNAILURL FROM MUSIC WHERE MUSIC_ID = #{musicId}")
    MusicBean getMusicById(int music_Id);

    // 음악 삭제
    @Delete("DELETE FROM MUSIC WHERE MUSIC_ID = #{musicId}")
    void deleteMusic(int music_Id);
}
