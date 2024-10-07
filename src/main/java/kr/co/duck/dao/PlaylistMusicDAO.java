package kr.co.duck.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.mapper.MusicRowMapper;

@Repository
public class PlaylistMusicDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 플레이리스트에 음악 추가
    public void addMusicToPlaylist(int playlistId, int musicId, int playOrder) {
        String sql = "INSERT INTO Playlist_Music (playlistId, musicId, playOrder) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, playlistId, musicId, playOrder);
    }

    // 특정 플레이리스트의 모든 음악을 조회 (재생 순서대로)
    public List<MusicBean> getMusicFromPlaylist(int playlistId) {
        String sql = "SELECT m.* FROM Music m JOIN Playlist_Music pm ON m.MUSIC_id = pm.musicId WHERE pm.playlistId = ? ORDER BY pm.playOrder";
        return jdbcTemplate.query(sql, new Object[]{playlistId}, new MusicRowMapper());
    }

    // 특정 플레이리스트에서 곡 삭제
    public void removeMusicFromPlaylist(int playlistId, int musicId) {
        String sql = "DELETE FROM Playlist_Music WHERE playlistId = ? AND musicId = ?";
        jdbcTemplate.update(sql, playlistId, musicId);
    }
}
