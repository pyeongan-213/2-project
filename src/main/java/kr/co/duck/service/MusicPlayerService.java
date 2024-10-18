package kr.co.duck.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.dao.MusicPlayerDAO;
import kr.co.duck.dao.PlaylistMusicDAO;
import kr.co.duck.domain.Music;

@Service
public class MusicPlayerService {

	@Autowired
	private MusicPlayerDAO musicPlayerDAO;

	@Autowired
    private PlaylistMusicDAO playlistMusicDAO;
	
	// 특정 플레이리스트에서 재생 순서에 해당하는 곡을 가져오는 메서드
	public Music getMusicByPlayOrder(int playlistId, int playOrder) {
		return musicPlayerDAO.getMusicByPlayOrder(playlistId, playOrder);
	}

	// 특정 플레이리스트의 모든 곡을 가져오는 메서드
	public List<Music> getAllMusicInPlaylist(int playlistId) {
		return musicPlayerDAO.getAllMusicInPlaylist(playlistId);
	}

	// 곡 삭제 서비스 메서드
	public void deleteMusicById(int musicId) {
		musicPlayerDAO.deleteMusicById(musicId);
	}

	// 플레이리스트의 순서를 업데이트하는 메서드
	@Transactional // 트랜잭션 관리
	public void updatePlaylistOrder(List<Integer> order) {
		for (int i = 0; i < order.size(); i++) {
			playlistMusicDAO.updatePlayOrder(order.get(i), i + 1); // 각 곡의 playOrder를 업데이트
		}
	}
}
