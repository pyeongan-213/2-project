package kr.co.duck.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.MusicBean;

@Service
public class PlaylistService {

    @Autowired
    private YouTubeService youtubeService;

    // YouTube에서 검색 후 결과를 가져와서 플레이리스트에 추가 (검색 결과만 반환)
    public List<MusicBean> searchAndAddToPlaylist(String query) {
        List<MusicBean> searchResults = new ArrayList<>();
        try {
            // YouTube API를 통해 검색 결과 가져오기
            searchResults = youtubeService.searchYouTube(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResults;  // 검색 결과를 반환
    }

    // 전체 플레이리스트를 반환
    public List<MusicBean> getPlaylist() {
        // 플레이리스트의 실제 데이터를 가져오는 로직 구현
        return new ArrayList<>();  // 현재는 빈 리스트로 반환
    }
}
