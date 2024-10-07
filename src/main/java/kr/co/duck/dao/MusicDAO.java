package kr.co.duck.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.mapper.MusicRowMapper;

@Repository
public class MusicDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 새로운 곡을 저장
    public int saveMusic(MusicBean musicBean) {
        String sql = "INSERT INTO Music (music_Name, artist, music_Length, videoUrl, thumbnailUrl) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, musicBean.getMusicName(), musicBean.getArtist(), musicBean.getMusicLength(), musicBean.getVideoUrl(), musicBean.getThumbnailUrl());
        
        // 새로 생성된 musicId 반환
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // 음악을 ID로 조회
    public MusicBean getMusicById(int musicId) {
        String sql = "SELECT * FROM Music WHERE MUSIC_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{musicId}, new MusicRowMapper());
    }
}
