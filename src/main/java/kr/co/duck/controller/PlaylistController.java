package kr.co.duck.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.beans.MusicBean;
import kr.co.duck.beans.PlaylistBean;
import kr.co.duck.service.PlaylistManagementService;
import kr.co.duck.service.PlaylistService;

@Controller
public class PlaylistController {

	@Autowired
	private PlaylistService playlistService;

	@Resource(name = "loginMemberBean")
	private MemberBean loginMemberBean;
	
	
	@Autowired
	private PlaylistManagementService playlistManagementService;

	// YouTube 검색 요청 처리 및 플레이리스트 가져오기
	@GetMapping("/youtubeSearch")
	public String searchYouTube(@RequestParam("query") String query, Model model,
			@SessionAttribute("loginMemberBean") MemberBean member) {
		// YouTube 검색 결과 가져오기
		List<MusicBean> searchResults = playlistService.searchAndAddToPlaylist(query);

		// 로그인한 사용자의 member_id로 플레이리스트 가져오기
		List<PlaylistBean> userPlaylists = playlistService.getUserPlaylists(member.getMember_id());

		model.addAttribute("searchResults", searchResults);
		model.addAttribute("userPlaylists", userPlaylists);

		return "playlist/youtubeSearch";
	}

	// 플레이리스트 페이지로 이동
	@GetMapping("/playlist")
	public String showPlaylist(Model model) {
		List<MusicBean> playlist = playlistService.getPlaylist();
		model.addAttribute("playlist", playlist);
		return "playlist/playlist"; // 플레이리스트를 playlist.jsp로 전달
	}

	// 플레이리스트 목록 조회
	@GetMapping("/playlist/list")
	public String showPlaylistList(Model model, @SessionAttribute("loginMemberBean") MemberBean member) {
		List<PlaylistBean> playlists = playlistService.getUserPlaylists(member.getMember_id());
		model.addAttribute("playlists", playlists); // JSP에 전달
		return "playlist/list"; // playlist/list.jsp로 이동
	}

	// 플레이리스트에 YouTube 동영상 추가
	@PostMapping("/playlist/addToPlaylist")
	public String addToPlaylist(@RequestParam("playlistId") int playlistId, @RequestParam("videoUrl") String videoUrl,
			@RequestParam("musicName") String musicName, @RequestParam("artist") String artist,
			@RequestParam("thumbnailUrl") String thumbnailUrl, @SessionAttribute("loginMemberBean") MemberBean member) {

		// MusicBean 생성 및 정보 설정
		MusicBean music = new MusicBean();
		music.setVideoUrl(videoUrl);
		music.setMusicName(musicName);
		music.setArtist(artist);
		music.setThumbnailUrl(thumbnailUrl);

		// 플레이리스트에 음악 추가
		playlistService.addMusicToPlaylist(playlistId, music, member.getMember_id());

		// 플레이리스트 페이지로 리다이렉트
		return "redirect:/playlist/view?playlistId=" + playlistId;
	}

	// 새 플레이리스트 생성
	@PostMapping("/playlist/create")
	public String createPlaylist(@RequestParam("playlistName") String playlistName,
			@SessionAttribute("loginMemberBean") MemberBean member) {
		// 새 플레이리스트 생성
		PlaylistBean playlist = new PlaylistBean();
		playlist.setPlaylistName(playlistName);
		playlist.setMemberId(member.getMember_id()); // 로그인한 사용자의 member_id 사용

		// 서비스 호출하여 플레이리스트 저장
		playlistService.createPlaylist(playlist);

		// 플레이리스트 목록 페이지로 리다이렉트
		return "redirect:/playlist/list";
	}

	public PlaylistController(PlaylistService playlistService) {
		this.playlistService = playlistService;
	}

	// 플레이리스트 삭제
	@PostMapping("/playlist/delete")
	public String deletePlaylist(@RequestParam("playlistId") int playlistId) {
		playlistManagementService.deletePlaylist(playlistId);
		return "redirect:/playlist/list"; // 플레이리스트 목록으로 리다이렉트
	}

}
