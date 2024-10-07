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

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    // YouTube 검색 요청 처리
    @GetMapping("/youtubeSearch")
    public String searchYouTube(@RequestParam("query") String query, Model model) {
        try {
            // YouTube 검색 결과 가져오기
            List<MusicBean> musicBeans = playlistService.searchAndAddToPlaylist(query);
            model.addAttribute("searchResults", musicBeans);  // 검색 결과를 JSP로 전달
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "YouTube 검색 중 오류가 발생했습니다.");
        }
        return "playlist/youtubeSearch";  // 검색 결과 페이지로 포워딩
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
        try {
            // YouTube API에서 videoId로 검색해서 곡 정보를 가져오기
            List<MusicBean> musicBeans = playlistService.searchAndAddToPlaylist(musicName); // 곡 정보 검색
            if (!musicBeans.isEmpty()) {
                MusicBean musicBean = musicBeans.get(0);  // 첫 번째 검색 결과를 사용
                // 여기서 DB에 해당 음악 정보를 저장하는 로직 추가
                // (예: musicDAO.saveMusic(musicBean) 호출)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/playlist";  // 곡 추가 후 플레이리스트 페이지로 리다이렉트
    }


}
