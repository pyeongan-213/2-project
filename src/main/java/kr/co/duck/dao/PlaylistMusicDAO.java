package kr.co.duck.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PlaylistMusicDAO {

	// playOrder 값을 업데이트하는 메서드
	@Update("UPDATE playlist_music SET playOrder = #{playOrder} WHERE music_id = #{musicId}")
	void updatePlayOrder(@Param("musicId") int musicId, @Param("playOrder") int playOrder);
}
