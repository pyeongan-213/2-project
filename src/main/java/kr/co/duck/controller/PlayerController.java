package kr.co.duck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.domain.Music;
import kr.co.duck.domain.Playlist;
import kr.co.duck.service.PlayerService;

@Controller
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	// 모든 페이지에서 로그인한 사용자의 플레이리스트를 사용할 수 있도록 ModelAttribute 사용
	@ModelAttribute("playlists")
	public List<Playlist> getUserPlaylists(HttpSession session) {
		// 세션에서 로그인한 사용자 정보를 가져옴
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");
		if (member == null) {
			System.out.println("로그인한 사용자가 없습니다.");
			return Collections.emptyList();
		} else {
			System.out.println("로그인한 사용자 ID: " + member.getMember_id());
		}

		// 로그인한 사용자의 memberId로 플레이리스트를 조회하여 반환
		int member_Id = member.getMember_id();
		return playerService.getPlaylistsByMemberId(member_Id);
	}

	// 회원의 플레이리스트 목록을 가져와서 보여줌
	@GetMapping("/playlist/selectPlaylist")
	public String selectPlaylist(Model model, HttpSession session) {
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");

		// 로그인하지 않은 경우 로그인 페이지로 리다이렉트
		if (member == null) {
			return "redirect:/login";
		}

		// 사용자의 플레이리스트 가져오기
		int memberId = member.getMember_id();
		List<Playlist> playlists = playerService.getPlaylistsByMemberId(memberId);
		model.addAttribute("playlists", playlists);

		// Ajax 요청에 대한 응답으로 플레이리스트 HTML만 반환
		return "playlist/sidePlaylist"; // JSP 파일을 sidePlaylist.jsp로 지정
	}

	// 특정 플레이리스트의 음악 목록을 표시
	@GetMapping("/playlist/playlist")
	public String showPlaylist(Model model, @RequestParam("playlistId") int playlistId, HttpSession session) {
		List<Music> musicList = playerService.getMusicListByPlaylistId(playlistId);
		MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");
		if (member == null)
			return "redirect:/login"; // 로그인하지 않은 경우 리다이렉트 }

		model.addAttribute("musicList", musicList); // 음악 목록을 모델에 추가
		model.addAttribute("playlistId", playlistId); // 현재 플레이리스트 ID도 전달
		return "playlist/playlist"; // playlist.jsp로 이동
	}

}
