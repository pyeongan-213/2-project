package kr.co.duck.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.dao.MusicPlayerDAO;
import kr.co.duck.domain.Music;

@Service
public class MusicPlayerService {

    @Autowired
    private MusicPlayerDAO musicPlayerDAO;

    // 특정 플레이리스트에서 재생 순서에 해당하는 곡을 가져오는 메서드
    public Music getMusicByPlayOrder(int playlistId, int playOrder) {
        return musicPlayerDAO.getMusicByPlayOrder(playlistId, playOrder);
    }

    // 특정 플레이리스트의 모든 곡을 가져오는 메서드
    public List<Music> getAllMusicInPlaylist(int playlistId) {
        return musicPlayerDAO.getAllMusicInPlaylist(playlistId);
    }
}
