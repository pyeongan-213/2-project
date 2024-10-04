package kr.co.duck.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import kr.co.duck.beans.MusicBean;

public interface MusicMapper {

    // MusicBean 저장
    @Insert("INSERT INTO Music (id, musicName, artist, musicLength, videoUrl, thumbnailUrl) " +
            "VALUES (music_seq.nextval, #{musicName}, #{artist}, #{musicLength}, #{videoUrl}, #{thumbnailUrl})")
    void insertMusic(MusicBean musicBean);

    // ID로 MusicBean 조회
    @Select("SELECT id, musicName, artist, musicLength, videoUrl, thumbnailUrl FROM Music WHERE id = #{musicId}")
    MusicBean getMusicById(int musicId);
}
