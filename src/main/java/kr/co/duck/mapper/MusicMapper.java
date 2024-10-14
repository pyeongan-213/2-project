package kr.co.duck.mapper;

import org.apache.ibatis.annotations.Insert;
import kr.co.duck.beans.MusicBean;

public interface MusicMapper {

    // 음악 추가
    @Insert("INSERT INTO MUSIC (MUSIC_ID, MUSIC_NAME, ARTIST, videoURL, thumbnailURL) " +
            "VALUES (music_seq.NEXTVAL, #{musicName}, #{artist}, #{videoUrl}, #{thumbnailUrl})")
    void insertMusic(MusicBean music);
}
