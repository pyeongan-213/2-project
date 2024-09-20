package kr.co.duck.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class ManiaDBService {

    private static final String API_URL = "https://www.maniadb.com/api/search/%s/?sr=song&key=example&v=0.5";

    public List<Music> searchMusic(String query) {
        // API 호출 및 결과 가져오기 (JSON 형식으로)
        String jsonResult = callManiaDBApi(query);
        
        // JSON 결과가 비어 있거나 배열이 아닌 경우 처리
        if (jsonResult.isEmpty() || !jsonResult.startsWith("[")) {
            // 적절한 예외 처리 또는 기본값 반환
            System.out.println("API 호출 실패 또는 응답이 비어 있습니다: " + jsonResult);
            return new ArrayList<>(); // 빈 리스트 반환
        }

        // JSON을 List<Music>으로 변환
        Gson gson = new Gson();
        return gson.fromJson(jsonResult, new TypeToken<List<Music>>() {}.getType());
    }

    private String callManiaDBApi(String query) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(API_URL, query);
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
            return ""; // 예외 발생 시 빈 문자열 반환
        }
    }

    // Music 클래스 정의
    public static class Music {
        private String title;
        private String artist;

        // getters and setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }
    }
}
