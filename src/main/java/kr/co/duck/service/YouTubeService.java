package kr.co.duck.service;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import kr.co.duck.beans.MusicBean;

@Service
public class YouTubeService {

    private static final String API_KEY = "AIzaSyBLzIORdM8y-Clju3H0X7lVciqTLMKJ7eg";  // YouTube API 키
    private static final String APPLICATION_NAME = "YOUR_APP_NAME";
    private static final long MAX_RESULTS = 5;  // 최대 검색 결과 개수
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // YouTube 검색 및 동영상 정보 가져오기
    public List<MusicBean> searchYouTube(String queryTerm) throws GeneralSecurityException, IOException {
        // YouTube 검색 결과 가져오기
        List<SearchResult> searchResults = getSearchResults(queryTerm);

        // 동영상 ID 목록 만들기
        List<String> videoIds = new ArrayList<>();
        for (SearchResult result : searchResults) {
            videoIds.add(result.getId().getVideoId());
        }

        // 동영상 길이 포함한 동영상 정보 가져오기
        List<Video> videoDetails = getVideoDetails(videoIds);

        // 검색 결과와 동영상 정보를 MusicBean으로 변환
        List<MusicBean> musicBeans = new ArrayList<>();
        for (int i = 0; i < searchResults.size(); i++) {
            SearchResult result = searchResults.get(i);
            Video video = videoDetails.get(i);

            MusicBean musicBean = new MusicBean();
            musicBean.setMusicName(result.getSnippet().getTitle());  // 동영상 제목
            musicBean.setArtist(result.getSnippet().getChannelTitle());  // 채널 이름
            musicBean.setMusicLength(formatDuration(video.getContentDetails().getDuration()));  // 동영상 길이
            musicBean.setVideoUrl("https://www.youtube.com/watch?v=" + result.getId().getVideoId());  // 비디오 URL
            musicBean.setThumbnailUrl(result.getSnippet().getThumbnails().getDefault().getUrl());  // 썸네일 URL

            musicBeans.add(musicBean);
        }

        return musicBeans;
    }

    // YouTube 검색 결과 가져오기
    private List<SearchResult> getSearchResults(String queryTerm) throws GeneralSecurityException, IOException {
        YouTube youtubeService = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.Search.List searchRequest = youtubeService.search().list("snippet");
        searchRequest.setQ(queryTerm);
        searchRequest.setType("video");
        searchRequest.setKey(API_KEY);
        searchRequest.setMaxResults(MAX_RESULTS);

        // 검색 결과 실행
        return searchRequest.execute().getItems();
    }

    // Videos API로 동영상 정보 가져오기
    private List<Video> getVideoDetails(List<String> videoIds) throws IOException, GeneralSecurityException {
        YouTube youtubeService = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.Videos.List videoRequest = youtubeService.videos().list("contentDetails");
        videoRequest.setId(String.join(",", videoIds));
        videoRequest.setKey(API_KEY);

        // 동영상 정보 실행
        VideoListResponse videoResponse = videoRequest.execute();
        return videoResponse.getItems();
    }

    // ISO 8601 형식의 동영상 길이를 변환하는 메서드
    private String formatDuration(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    
 // YouTube Videos API로 동영상 ID로 정보를 가져오는 메서드
    public MusicBean getSongByVideoId(String videoId) {
        try {
            // YouTube API 클라이언트 생성
            YouTube youtubeService = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Videos API를 통해 동영상 정보 가져오기
            YouTube.Videos.List videoRequest = youtubeService.videos().list("snippet,contentDetails");
            videoRequest.setId(videoId);
            videoRequest.setKey(API_KEY);

            // API 요청 실행 및 결과 가져오기
            VideoListResponse videoResponse = videoRequest.execute();
            List<Video> videoList = videoResponse.getItems();

            if (!videoList.isEmpty()) {
                Video video = videoList.get(0);  // 첫 번째 동영상 정보 가져오기

                // 동영상 정보를 MusicBean으로 변환
                String title = video.getSnippet().getTitle();  // 동영상 제목
                String channelTitle = video.getSnippet().getChannelTitle();  // 채널 이름 (아티스트)
                String duration = video.getContentDetails().getDuration();  // ISO 8601 형식의 동영상 길이
                String formattedDuration = formatDuration(duration);  // 형식을 변환한 동영상 길이
                String thumbnailUrl = video.getSnippet().getThumbnails().getDefault().getUrl();  // 썸네일 URL
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;  // YouTube 동영상 URL

                // MusicBean 생성 및 반환
                return new MusicBean(title, channelTitle, formattedDuration, videoUrl, thumbnailUrl);
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        // 오류가 발생하거나 동영상이 없을 경우 null 반환
        return null;
    }
}
