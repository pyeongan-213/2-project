package kr.co.duck.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.service.YouTubeService;

@Controller
public class PlaylistController {

	@Autowired
	private YouTubeService youtubeService;

	@GetMapping("/youtubeSearch")
	public String searchYouTube(@RequestParam(value = "query", required = false) String query, Model model) {
		try {
			List<MusicBean> musicBeans = youtubeService.searchYouTube(query);
			model.addAttribute("musicBeans", musicBeans);
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "An error occurred while searching YouTube.");
		}
		return "playlist/playlist"; // JSP 경로
	}
}
