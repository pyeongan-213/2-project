package kr.co.duck.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.beans.PlaylistBean;
import kr.co.duck.dao.MusicDAO;
import kr.co.duck.dao.PlaylistDAO;
import kr.co.duck.youtube.YouTubeAPI;

@Service
public class PlaylistManagementService {

	@Autowired
	private PlaylistDAO playlistDAO;
	
	@Autowired
	private MusicDAO musicDAO;

	@Autowired
	private YouTubeAPI youtubeAPI;

	// 플레이리스트 생성
	public void createPlaylist(PlaylistBean playlist) {
		playlistDAO.insertPlaylist(playlist);
	}

	// 모든 플레이리스트 조회
	public List<PlaylistBean> getAllPlaylists() {
		return playlistDAO.getAllPlaylists();
	}

	// YouTube API를 통해 videoId로 음악 정보 가져오기
	public MusicBean getSongByVideoId(String videoId) throws Exception {
		return youtubeAPI.getSongByVideoId(videoId);
	}

	// YouTube에서 가져온 음악을 플레이리스트에 추가
	public void addMusicToPlaylist(int playlistId, MusicBean music, int memberId) {
	    // 1. Music 테이블에 음악을 저장
	    musicDAO.insertMusic(music);

	    // 2. 플레이리스트에 음악 추가 (playlistId, musicId, memberId 연결)
	    playlistDAO.addMusicToPlaylist(playlistId, music.getMusicId(), memberId);
	}



	// 플레이리스트에서 음악 삭제
	public void removeMusicFromPlaylist(int playlistId, int musicId) {
		playlistDAO.removeMusicFromPlaylist(playlistId, musicId);
	}

	// 플레이리스트의 음악 순서 변경
	public void updateMusicOrderInPlaylist(int playlistId, List<Integer> musicIdList) {
		int order = 1;
		for (Integer musicId : musicIdList) {
			playlistDAO.updateMusicOrder(playlistId, musicId, order++);
		}
	}

	// 플레이리스트 삭제
	public void deletePlaylist(int playlistId) {
		playlistDAO.deletePlaylist(playlistId);
	}
}
