package kr.co.duck.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.beans.PlaylistBean;
import kr.co.duck.dao.MusicDAO;
import kr.co.duck.dao.PlaylistDAO;

@Service
public class PlaylistService {

    @Autowired
    private YouTubeService youtubeService;

    @Autowired
    private MusicDAO musicDAO;

    @Autowired
    private PlaylistDAO playlistDAO;

    // YouTube에서 검색 후 결과를 가져와서 플레이리스트에 추가 (검색 결과 반환)
    public List<MusicBean> searchAndAddToPlaylist(String query) {
        List<MusicBean> searchResults = new ArrayList<>();
        try {
            // YouTube API를 통해 검색 결과 가져오기
            searchResults = youtubeService.searchYouTube(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResults;  // 검색 결과 반환
    }

    // YouTube 동영상을 플레이리스트에 추가
    public void addMusicToPlaylist(int playlistId, MusicBean music, int memberId) {
        // MUSIC 테이블에 음악을 삽입하고, 자동 생성된 musicId를 얻음
    	String ThumbnailURLRaw = music.getThumbnailUrl();
    	String ThunbnailUrl = ThumbnailURLRaw.split("/watch?v=")[0];
    	music.setThumbnailUrl(ThunbnailUrl);
        musicDAO.insertMusic(music);

        // 생성된 musicId를 이용해 PLAYLIST_MUSIC 테이블에 음악 추가
        playlistDAO.addMusicToPlaylist(playlistId, music.getMusicId(), memberId);
    }



    // 전체 플레이리스트를 반환
    public List<MusicBean> getPlaylist() {
        // 플레이리스트의 실제 데이터를 가져오는 로직 구현
        return new ArrayList<>();  // 현재는 빈 리스트로 반환
    }
    
    public void createPlaylist(PlaylistBean playlist) {
        // PlaylistDAO를 통해 DB에 플레이리스트 저장
        playlistDAO.insertPlaylist(playlist);
    }
    
	/*
	 * public List<PlaylistBean> getUserPlaylists(int memberId) { // PlaylistDAO에서
	 * member_id로 플레이리스트를 가져오는 메서드 호출 return
	 * playlistDAO.getPlaylistsByMemberId(memberId); }
	 * 
	 * // 특정 플레이리스트의 음악 목록 가져오기 public List<MusicBean> getMusicListForPlaylist(int
	 * playlistId) { return playlistDAO.getMusicListByPlaylistId(playlistId); }
	 */
}
