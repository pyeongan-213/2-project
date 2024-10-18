package kr.co.duck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import kr.co.duck.dao.PlaylistDAO;
import kr.co.duck.domain.Music;
import kr.co.duck.domain.Playlist;

@Service
public class PlayerService {

    @Autowired
    private PlaylistDAO playlistDAO;

    // 특정 플레이리스트에 속한 음악 목록을 가져오는 메소드
    public List<Music> getMusicListByPlaylistId(int playlistId) {
        return playlistDAO.getMusicListByPlaylistId(playlistId);  // DAO 호출
    }
    
    // 회원의 플레이리스트 목록을 가져오는 메소드
    public List<Playlist> getPlaylistsByMemberId(int memberId) {
        return playlistDAO.getPlaylistsByMemberId(memberId);
    }
}
