package kr.co.duck.controller;

import com.google.api.services.youtube.model.SearchResult;

import kr.co.duck.service.PlaylistService;
import kr.co.duck.service.YouTubeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private YouTubeService youTubeService;  // YouTubeService 주입

    @GetMapping("playlist/playlist")
    public String showPlaylist(Model model) {
        model.addAttribute("playlist", playlistService.getPlaylist());
        return "playlist/playlist"; // playlist.jsp를 반환
    }

    // YouTube 검색 결과 가져오기
    @GetMapping("/youtubeSearch")
    public String searchYouTube(@RequestParam("query") String query, Model model) {
        try {
            // YouTube API를 사용하여 검색 결과 가져오기
            List<SearchResult> results = youTubeService.searchYouTube(query); // 수정된 부분
            // 결과를 JSP에 전달
            model.addAttribute("results", results);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "검색 중 오류가 발생했습니다.");
        }

        return "playlist/playlist";  // JSP 파일 위치
    }
}
