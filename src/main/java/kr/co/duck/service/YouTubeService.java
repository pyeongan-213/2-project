package kr.co.duck.service;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

@Service
public class YouTubeService {

    private static final String API_KEY = "AIzaSyBLzIORdM8y-Clju3H0X7lVciqTLMKJ7eg";  // API 키를 여기에 입력
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public List<SearchResult> searchYouTube(String queryTerm) throws GeneralSecurityException, IOException {
        YouTube youtubeService = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, null)
                .setApplicationName("your-app-name")
                .build();

        // YouTube Data API v3에서 검색 요청 설정
        YouTube.Search.List searchRequest = youtubeService.search()
                .list("snippet")
                .setQ(queryTerm)
                .setKey(API_KEY)
                .setType("video")
                .setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");

        // 검색 결과 반환
        SearchListResponse searchResponse = searchRequest.execute();
        return searchResponse.getItems();
    }
}
