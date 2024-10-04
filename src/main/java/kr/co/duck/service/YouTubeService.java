package kr.co.duck.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.duck.beans.MusicBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {

	
	@Autowired
	private YouTube youtube;

	

    // YouTube API 키 (Google API 콘솔에서 발급받은 API 키)
    private final String apiKey = "AIzaSyBLzIORdM8y-Clju3H0X7lVciqTLMKJ7eg";

    // YouTube에서 검색 쿼리로 결과 가져오기
    public List<MusicBean> searchYouTube(String query) {
        List<MusicBean> musicBeans = new ArrayList<>();
        try {
        	if (youtube == null) {
        	    System.out.println("YouTube 객체가 주입되지 않았습니다.");
        	    throw new IllegalStateException("YouTube 객체가 null 상태입니다.");
        	}

        	
            // 로그 추가: 검색 시작 전
            System.out.println("YouTube 검색 요청 시작: " + query);

            // YouTube 검색 요청 구성
            YouTube.Search.List search = youtube.search().list("snippet");
            search.setKey(apiKey);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(5L); // 최대 5개의 결과만 가져옴

            // 로그 추가: API 요청 실행 전
            System.out.println("YouTube API 요청 실행 중...");
            
            // YouTube 검색 실행
            SearchListResponse response = search.execute();
            
            // 로그 추가: 응답 수신 후
            System.out.println("YouTube API 요청 성공, 결과 수: " + response.getItems().size());

            List<SearchResult> results = response.getItems();

            // 검색 결과를 MusicBean 리스트로 변환
            for (SearchResult result : results) {
                MusicBean musicBean = new MusicBean();
                musicBean.setMusicName(result.getSnippet().getTitle());
                musicBean.setArtist(result.getSnippet().getChannelTitle());
                musicBean.setThumbnailUrl(result.getSnippet().getThumbnails().getDefault().getUrl());
                musicBean.setVideoUrl("https://www.youtube.com/watch?v=" + result.getId().getVideoId());

                // YouTube API는 동영상 길이를 직접 반환하지 않으므로 추가 API 요청이 필요할 수 있음
                String videoId = result.getId().getVideoId();
                String videoLength = getVideoDurationByVideoId(videoId);
                musicBean.setMusicLength(videoLength);

                // 로그 추가: 결과마다 정보 출력
                System.out.println("노래 제목: " + musicBean.getMusicName() + ", 아티스트: " + musicBean.getArtist());

                musicBeans.add(musicBean);
            }

        } catch (IOException e) {
            // 로그 추가: 예외 발생 시 출력
            e.printStackTrace();
            System.out.println("YouTube API 호출 중 오류 발생: " + e.getMessage());
        }

        return musicBeans;
    }


    // videoId로 동영상의 길이를 가져오는 메서드
    private String getVideoDurationByVideoId(String videoId) {
        try {
            // 로그 추가: 동영상 길이 요청 시작
            System.out.println("동영상 길이 요청 시작: videoId = " + videoId);

            // YouTube 비디오 정보 요청 구성
            YouTube.Videos.List videoRequest = youtube.videos().list("contentDetails");
            videoRequest.setId(videoId);
            videoRequest.setKey(apiKey);

            // YouTube 비디오 정보 실행
            VideoListResponse videoResponse = videoRequest.execute();

            // 로그 추가: 응답 수신 후
            System.out.println("비디오 응답 수신: " + videoResponse.getItems().size() + "개");

            Video video = videoResponse.getItems().get(0); // 첫 번째 결과 사용

            // 로그 추가: 동영상 길이 출력
            System.out.println("동영상 길이: " + video.getContentDetails().getDuration());

            // 동영상 길이 반환
            return parseYouTubeDuration(video.getContentDetails().getDuration());
        } catch (IOException e) {
            // 로그 추가: 예외 발생 시 출력
            e.printStackTrace();
            System.out.println("동영상 길이 가져오기 중 오류 발생: " + e.getMessage());
        }

        return "Unknown"; // 오류 시 기본 값
    }


    // ISO 8601 형식의 동영상 길이를 포맷팅하는 메서드
    private String parseYouTubeDuration(String duration) {
        // ISO 8601 형식 (예: PT1H2M3S)을 적절히 파싱하여 읽을 수 있는 형식으로 변환
        // 간단한 변환 예시 (여기서는 시간, 분, 초만 사용)
        duration = duration.replace("PT", "").replace("H", ":").replace("M", ":").replace("S", "");
        return duration;
    }

    // videoId로 곡 정보 가져오기
    public MusicBean getSongByVideoId(String videoId) {
        MusicBean musicBean = new MusicBean();
        try {
            // 로그 추가: videoId로 곡 정보 요청 시작
            System.out.println("곡 정보 요청 시작: videoId = " + videoId);

            // YouTube 비디오 정보를 가져오는 API 호출 구성
            YouTube.Videos.List videoRequest = youtube.videos().list("snippet,contentDetails");
            videoRequest.setId(videoId);
            videoRequest.setKey(apiKey);

            // YouTube 비디오 정보 실행
            VideoListResponse videoResponse = videoRequest.execute();

            // 로그 추가: 응답 수신 후
            System.out.println("곡 정보 응답 수신: " + videoResponse.getItems().size() + "개");

            Video video = videoResponse.getItems().get(0); // 첫 번째 결과를 사용

            // MusicBean에 데이터 설정
            musicBean.setMusicName(video.getSnippet().getTitle());
            musicBean.setArtist(video.getSnippet().getChannelTitle());
            musicBean.setThumbnailUrl(video.getSnippet().getThumbnails().getDefault().getUrl());
            musicBean.setVideoUrl("https://www.youtube.com/watch?v=" + videoId);

            // 동영상 길이 설정 (ISO 8601 형식)
            String duration = video.getContentDetails().getDuration();
            musicBean.setMusicLength(parseYouTubeDuration(duration));

            // 로그 추가: 곡 정보 출력
            System.out.println("곡 정보: 제목 = " + musicBean.getMusicName() + ", 아티스트 = " + musicBean.getArtist());

        } catch (Exception e) {
            // 로그 추가: 예외 발생 시 출력
            e.printStackTrace();
            System.out.println("곡 정보 가져오기 중 오류 발생: " + e.getMessage());
        }
        return musicBean;
    }

}
