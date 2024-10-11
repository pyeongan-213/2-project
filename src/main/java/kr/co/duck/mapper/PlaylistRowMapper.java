package kr.co.duck.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import kr.co.duck.beans.Playlist;

public class PlaylistRowMapper implements RowMapper<Playlist> {

    @Override
    public Playlist mapRow(ResultSet rs, int rowNum) throws SQLException {
        Playlist playlist = new Playlist();
        playlist.setPlaylistId(rs.getInt("playlist_Id")); // 테이블의 컬럼명에 맞춰 매핑
        playlist.setPlaylistName(rs.getString("playlistName"));
        playlist.setCreatedBy(rs.getString("createdBy"));
        playlist.setCreatedDate(rs.getTimestamp("createdDate"));
        return playlist;
    }
}
