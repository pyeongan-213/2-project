package kr.co.duck.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.MusicBean;

@Service
public class PlaylistService {

    @Autowired
    private YouTubeService youtubeService;

    // 예시 플레이리스트
    private List<MusicBean> playlist = new ArrayList<>();

    // YouTube 검색 후 플레이리스트에 추가 (검색 결과만 반환)
    public List<MusicBean> searchAndAddToPlaylist(String query) {
        List<MusicBean> searchResults = new ArrayList<>();
        try {
            // YouTube API로 검색한 결과 가져오기
            searchResults = youtubeService.searchYouTube(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    // YouTube 동영상을 ID로 플레이리스트에 추가
    public void addSongToPlaylistByVideoId(String videoId) {
        try {
            // 동영상 ID로 곡 검색 후 플레이리스트에 추가
            MusicBean song = youtubeService.getSongByVideoId(videoId);
            playlist.add(song);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 전체 플레이리스트를 반환
    public List<MusicBean> getPlaylist() {
        return playlist;
    }

    // 특정 곡을 제목으로 삭제
    public boolean removeSongByTitle(String title) {
        return playlist.removeIf(song -> song.getMusicName().equalsIgnoreCase(title));
    }
}


