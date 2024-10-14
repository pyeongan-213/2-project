package kr.co.duck.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import kr.co.duck.beans.MusicBean;

@Component
public class YouTubeAPI {

    private static final String API_KEY = "AIzaSyBLzIORdM8y-Clju3H0X7lVciqTLMKJ7eg";  // YouTube API 키
    private static final String APPLICATION_NAME = "YourAppName";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private YouTube youtubeService;

    public YouTubeAPI() throws Exception {
        this.youtubeService = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                request -> {}
        ).setApplicationName(APPLICATION_NAME).build();
    }

    // YouTube 검색 결과를 반환하는 메서드
    public List<MusicBean> search(String query) throws IOException {
        List<MusicBean> musicBeans = new ArrayList<>();

        YouTube.Search.List search = youtubeService.search().list("snippet");
        search.setKey(API_KEY);
        search.setQ(query);
        search.setType("video");
        search.setMaxResults(5L);  // 최대 5개의 결과 가져오기

        SearchListResponse response = search.execute();
        List<SearchResult> results = response.getItems();

        for (SearchResult result : results) {
            MusicBean musicBean = new MusicBean();
            musicBean.setmusic_Name(result.getSnippet().getTitle());
            musicBean.setArtist(result.getSnippet().getChannelTitle());
            musicBean.setVideoUrl("https://www.youtube.com/watch?v=" + result.getId().getVideoId());
            musicBean.setThumbnailUrl(result.getSnippet().getThumbnails().getDefault().getUrl());
            musicBeans.add(musicBean);
        }

        return musicBeans;
    }

    // YouTube videoId로 곡 정보를 가져오는 메서드
    public MusicBean getSongByVideoId(String videoId) throws IOException {
        List<MusicBean> results = search("https://www.youtube.com/watch?v=" + videoId);
        return results.isEmpty() ? null : results.get(0);
    }
}
