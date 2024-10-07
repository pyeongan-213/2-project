package kr.co.duck.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.duck.beans.Playlist;
import kr.co.duck.mapper.PlaylistMapper;
import kr.co.duck.mapper.PlaylistRowMapper;

@Repository
public class PlaylistDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 새로운 플레이리스트 생성
    public int createPlaylist(String playlistName, String createdBy) {
        String sql = "INSERT INTO Playlist (playlistName, createdBy) VALUES (?, ?)";
        jdbcTemplate.update(sql, playlistName, createdBy);
        
        // 새로 생성된 playlistId 반환
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // 특정 사용자의 모든 플레이리스트 조회
    public List<Playlist> getPlaylistsByUser(String createdBy) {
        String sql = "SELECT * FROM Playlist WHERE createdBy = ?";
        return jdbcTemplate.query(sql, new Object[]{createdBy}, new PlaylistRowMapper());
    }
}
