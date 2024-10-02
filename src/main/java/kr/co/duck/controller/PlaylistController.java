package kr.co.duck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.service.PlaylistService;
import kr.co.duck.service.YouTubeService; // YouTubeService를 import

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private YouTubeService youtubeService; // YouTubeService를 Autowired로 주입

    // YouTube 검색 요청 처리
    @GetMapping("/youtubeSearch")
    public String searchYouTube(@RequestParam("query") String query, Model model) {
        try {
            // YouTube 검색 결과 가져오기
        	List<MusicBean> musicBeans = playlistService.searchAndAddToPlaylist(query);
        	System.out.println("검색된 결과 수: " + musicBeans.size());  // 결과 개수 출력
        	for (MusicBean musicBean : musicBeans) {
        	    System.out.println("제목: " + musicBean.getMusicName());
        	}
        	model.addAttribute("searchResults", musicBeans);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "YouTube 검색 중 오류가 발생했습니다.");
        }
        return "playlist/youtubeSearch"; 
    }

    // 플레이리스트 페이지로 이동
    @GetMapping("/playlist")
    public String showPlaylist(Model model) {
        List<MusicBean> playlist = playlistService.getPlaylist();
        model.addAttribute("playlist", playlist);
        return "playlist/playlist"; // 플레이리스트를 playlist.jsp로 전달
    }

    @PostMapping("/addToPlaylist")
    public String addToPlaylist(@RequestParam("videoId") String videoId, @RequestParam("musicName") String musicName) {
        // YouTube API에서 videoId로 음악 정보를 가져옴
        MusicBean musicBean = youtubeService.getSongByVideoId(videoId);

        // 순서값을 계산해서 재생목록에 추가 (예: 현재 목록 크기에 +1)
        int playOrder = playlistService.getPlaylist().size() + 1;
        playlistService.addMusicToPlaylist(musicBean, playOrder);

        return "redirect:/playlist"; // 곡 추가 후 플레이리스트로 리다이렉트
    }

}
