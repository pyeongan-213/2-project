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
	public String searchYouTube(@RequestParam(value = "query", required = false) String query, Model model) {
		try {
			// YouTube 검색 결과 가져오기
			List<MusicBean> musicBeans = playlistService.searchAndAddToPlaylist(query);
			model.addAttribute("searchResults", musicBeans);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "YouTube 검색 중 오류가 발생했습니다.");
		}
		return "playlist/youtubeSearch"; // 검색 결과를 youtubeSearch.jsp로 전달
	}

	// 플레이리스트 페이지로 이동
	@GetMapping("/playlist")
	public String showPlaylist(Model model) {
		List<MusicBean> playlist = playlistService.getPlaylist();
		model.addAttribute("playlist", playlist);
		return "playlist/playlist"; // 플레이리스트를 playlist.jsp로 전달
	}

	// 플레이리스트에 곡 추가
	@PostMapping("/addToPlaylist")
	public String addToPlaylist(@RequestParam("videoId") String videoId) {
		playlistService.addSongToPlaylistByVideoId(videoId);
		return "redirect:/playlist"; // 곡 추가 후 플레이리스트 페이지로 리다이렉트
	}

	// 플레이리스트에서 곡 삭제
	@PostMapping("/removeFromPlaylist")
	public String removeFromPlaylist(@RequestParam("title") String title) {
		playlistService.removeSongByTitle(title);
		return "redirect:/playlist"; // 곡 삭제 후 플레이리스트 페이지로 리다이렉트
	}
}
