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

    // 예시로 플레이리스트 데이터를 저장할 리스트
    private List<MusicBean> playlist = new ArrayList<>();

    // 기본 생성자
    public PlaylistService() {
        // 초기 플레이리스트에 곡을 몇 개 추가하는 예시
        playlist.add(new MusicBean("Song Title 1", "Artist 1", "3:30", "https://www.youtube.com/watch?v=example1", "https://example.com/image1.jpg"));
        playlist.add(new MusicBean("Song Title 2", "Artist 2", "4:15", "https://www.youtube.com/watch?v=example2", "https://example.com/image2.jpg"));
    }

    // 전체 플레이리스트를 반환하는 메서드
    public List<MusicBean> getPlaylist() {
        return playlist;
    }

    // 새로운 곡을 플레이리스트에 추가하는 메서드
    public void addSongToPlaylist(MusicBean song) {
        playlist.add(song);
    }

    // YouTube API에서 검색하여 곡을 플레이리스트에 추가
    public List<MusicBean> searchAndAddToPlaylist(String query) {
        List<MusicBean> searchResults = new ArrayList<>();
        try {
            // YouTubeService를 사용하여 검색 결과 가져오기
            searchResults = youtubeService.searchYouTube(query);
            // 검색 결과를 플레이리스트에 추가
            playlist.addAll(searchResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    // 특정 곡을 제목으로 검색하여 플레이리스트에서 삭제하는 메서드
    public boolean removeSongByTitle(String title) {
        return playlist.removeIf(song -> song.getMusicName().equalsIgnoreCase(title));
    }

    // 곡을 재정렬하거나 순서를 변경할 수 있는 메서드
    public void updatePlaylistOrder(List<MusicBean> newOrder) {
        this.playlist = newOrder;
    }

    // 제목을 기준으로 곡을 검색하는 메서드
    public MusicBean findSongByTitle(String title) {
        return playlist.stream()
                .filter(song -> song.getMusicName().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
}
