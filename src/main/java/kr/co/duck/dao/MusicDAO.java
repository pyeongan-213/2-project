package kr.co.duck.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.duck.beans.MusicBean;

@Repository
public class MusicDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 인스턴스 메서드로 정의
    public void saveMusic(MusicBean musicBean) {
        String sql = "INSERT INTO Music (MUSIC_ID, music_Name, artist, music_Length, videoUrl, thumbnailUrl) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, musicBean.getMusicId(), musicBean.getMusicName(), musicBean.getArtist(),
                            musicBean.getMusicLength(), musicBean.getVideoUrl(), musicBean.getThumbnailUrl());
    }
}
