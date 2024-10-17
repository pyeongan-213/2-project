package kr.co.duck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	// 회원의 플레이리스트 목록을 가져와서 보여줌
	@GetMapping("/playlist/selectPlaylist")
	public String selectPlaylist(Model model, HttpSession session) {
	    // 세션에서 loginMemberBean을 가져옴
	    MemberBean member = (MemberBean) session.getAttribute("loginMemberBean");

	    // 만약 세션에 로그인된 회원 정보가 없는 경우 처리
	    if (member == null) {
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트
	    }

	    // memberId를 사용하여 플레이리스트 가져오기
	    int memberId = member.getMember_id();
	    List<Playlist> playlists = playerService.getPlaylistsByMemberId(memberId);
	    model.addAttribute("playlists", playlists); // 플레이리스트 목록을 모델에 추가
	    System.out.println(playlists);
	    return "playlist/selectPlaylist"; // selectPlaylist.jsp로 이동
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
