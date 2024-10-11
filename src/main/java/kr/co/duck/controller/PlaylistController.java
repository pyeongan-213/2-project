package kr.co.duck.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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

	@Autowired
	private PlaylistManagementService playlistManagementService;

	// YouTube 검색 요청 처리 및 플레이리스트 가져오기
	@GetMapping("/youtubeSearch")
	public String searchYouTube(@RequestParam("query") String query, Model model, HttpSession session) {
		// 세션에서 loginMemberBean을 가져옴
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");

		// 로그인 정보가 없으면 로그인 페이지로 리다이렉트
		if (member == null) {
			return "redirect:/login";
		}

		// YouTube 검색 결과 가져오기
		List<MusicBean> searchResults = playlistService.searchAndAddToPlaylist(query);

		// 로그인한 사용자의 플레이리스트 가져오기
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

	// 플레이리스트 보기
	@GetMapping("/playlist/view")
	public String viewPlaylist(@RequestParam("playlistId") int playlistId, Model model, HttpSession session) {
		// 세션에서 로그인된 사용자 정보 가져오기
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");

		if (member == null) {
			return "redirect:/login"; // 로그인하지 않은 경우 리다이렉트
		}

		// 플레이리스트의 음악 목록 가져오기
		List<MusicBean> musicList = playlistService.getMusicListForPlaylist(playlistId);

		// 모델에 데이터를 추가하여 JSP로 전달
		model.addAttribute("musicList", musicList);
		model.addAttribute("playlistId", playlistId);

		return "playlist/playlist"; // playlist.jsp로 이동
	}

	// 플레이리스트 목록 조회
	@GetMapping("/playlist/list")
	public String showPlaylistList(HttpSession session, Model model) {
		// 세션에서 loginMemberBean을 가져옴
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");

		// 로그인된 사용자의 플레이리스트 가져오기
		List<PlaylistBean> playlists = playlistService.getUserPlaylists(member.getMember_id());
		model.addAttribute("playlists", playlists);

		return "playlist/list";
	}

	// 플레이리스트에 YouTube 동영상 추가
	@PostMapping("/playlist/addToPlaylist")
	public String addToPlaylist(@RequestParam("playlistId") int playlistId, @RequestParam("videoUrl") String videoUrl,
			@RequestParam("musicName") String musicName, @RequestParam("artist") String artist,
			@RequestParam("thumbnailUrl") String thumbnailUrl, HttpSession session) {

		// 세션에서 loginMemberBean을 가져옴
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");

		// MusicBean 생성 및 정보 설정
		MusicBean music = new MusicBean();
		music.setVideoUrl(videoUrl);
		music.setMusicName(musicName);
		music.setArtist(artist);
		music.setThumbnailUrl(thumbnailUrl);

		// 음악 ID를 설정하는 부분이 빠졌을 수 있습니다.
		// YouTube API 등을 통해 musicId를 제대로 가져오고 있는지 확인 필요
		System.out.println("Music ID: " + music.getMusicId()); // 값이 0인지 출력 확인

		// 플레이리스트에 음악 추가
		playlistService.addMusicToPlaylist(playlistId, music, member.getMember_id());

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
