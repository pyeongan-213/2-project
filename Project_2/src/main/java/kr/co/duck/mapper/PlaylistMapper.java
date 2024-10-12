package kr.co.duck.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import kr.co.duck.beans.PlaylistBean;

@Mapper
public interface PlaylistMapper {
    void insertPlaylist(PlaylistBean playlist);
    List<PlaylistBean> getAllPlaylists();
    void addMusicToPlaylist(int playlistId, int musicId);
    void removeMusicFromPlaylist(int playlistId, int musicId);
    void updateMusicOrder(int playlistId, int musicId, int playOrder);
    void deletePlaylist(int playlistId);
}