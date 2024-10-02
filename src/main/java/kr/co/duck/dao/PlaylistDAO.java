package kr.co.duck.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import kr.co.duck.mapper.PlaylistMapper;

@Repository
public class PlaylistDAO {

    @Autowired
    private PlaylistMapper playlistMapper;

    // 재생목록에 음악 추가
    public void addMusicToPlaylist(int musicId, int playOrder) {
        playlistMapper.insertMusicToPlaylist(musicId, playOrder);
    }

    // 재생목록 조회 (순서대로)
    public List<Integer> getPlaylistMusicIds() {
        return playlistMapper.getPlaylistMusicIds();
    }

    // 특정 곡을 재생목록에서 삭제
    public void removeMusicFromPlaylist(int musicId) {
        playlistMapper.deleteMusicFromPlaylist(musicId);
    }
}
