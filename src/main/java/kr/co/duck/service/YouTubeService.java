package kr.co.duck.service;

import java.util.List;
import java.util.ArrayList;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import org.springframework.stereotype.Service;

import kr.co.duck.beans.MusicBean;

@Service
public class YouTubeService {

    private static final String API_KEY = "AIzaSyBLzIORdM8y-Clju3H0X7lVciqTLMKJ7eg";  // YouTube API 키
    private static final String APPLICATION_NAME = "your-application-name";

    private YouTube youtube;

    public YouTubeService() {
        // YouTube 객체 초기화
        youtube = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), request -> {
        }).setApplicationName(APPLICATION_NAME).build();
    }

    // YouTube에서 검색 결과를 가져오는 메서드
    public List<MusicBean> searchYouTube(String query) {
        List<MusicBean> musicBeans = new ArrayList<>();
        try {
            System.out.println("YouTube API 호출 시작");
            System.out.println("검색어: " + query);

            // YouTube 검색 요청 구성
            YouTube.Search.List search = youtube.search().list("snippet");
            search.setKey(API_KEY);  // API 키 설정
            search.setQ(query +"MV");  // 검색어 설정
            search.setType("video");  // 비디오만 검색
            search.setMaxResults(5L);  // 결과 개수 설정

            System.out.println("YouTube API 요청 실행");

            // YouTube 검색 실행
            SearchListResponse response = search.execute();
            List<SearchResult> results = response.getItems();

            System.out.println("YouTube API 요청 성공");
            System.out.println("받은 결과 수: " + results.size());

            // 검색 결과를 MusicBean 리스트로 변환
            for (SearchResult result : results) {
                MusicBean musicBean = new MusicBean();
                musicBean.setmusic_Name(result.getSnippet().getTitle());
                musicBean.setArtist(result.getSnippet().getChannelTitle());
                musicBean.setThumbnailUrl(result.getSnippet().getThumbnails().getDefault().getUrl());
                musicBean.setVideoUrl("https://www.youtube.com/watch?v=" + result.getId().getVideoId());

                System.out.println("검색 결과: " + musicBean.getmusic_Name() + " - " + musicBean.getArtist());

                musicBeans.add(musicBean);
            }

        } catch (Exception e) {
            System.out.println("YouTube API 호출 중 오류 발생");
            e.printStackTrace();
        }
        return musicBeans;
    }
}
