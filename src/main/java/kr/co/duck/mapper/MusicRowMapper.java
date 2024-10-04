package kr.co.duck.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import kr.co.duck.beans.MusicBean;

public class MusicRowMapper implements RowMapper<MusicBean> {
    @Override
    public MusicBean mapRow(ResultSet rs, int rowNum) throws SQLException {
        MusicBean musicBean = new MusicBean();
        musicBean.setMusicName(rs.getString("musicName"));
        musicBean.setArtist(rs.getString("artist"));
        musicBean.setMusicLength(rs.getString("musicLength"));
        musicBean.setVideoUrl(rs.getString("videoUrl"));
        musicBean.setThumbnailUrl(rs.getString("thumbnailUrl"));
        return musicBean;
    }
}
