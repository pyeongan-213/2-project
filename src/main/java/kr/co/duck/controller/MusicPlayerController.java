package kr.co.duck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.duck.domain.Music;
import kr.co.duck.service.MusicPlayerService;

@Controller
@RequestMapping("/musicPlayer")
public class MusicPlayerController {

	@Autowired
	private MusicPlayerService musicPlayerService;

	// 특정 재생 순서에 해당하는 곡을 가져오는 API
	@GetMapping("/play/{playlistId}/{playOrder}")
	@ResponseBody
	public Music playMusicByOrder(@PathVariable("playlistId") int playlistId,
			@PathVariable("playOrder") int playOrder) {
		// 플레이리스트에서 해당 순서의 곡을 가져와서 반환
		return musicPlayerService.getMusicByPlayOrder(playlistId, playOrder);
	}

	// 특정 플레이리스트의 모든 곡을 가져오는 메서드
	@GetMapping("/playlist/{playlistId}")
	@ResponseBody
	public List<Music> getAllMusicInPlaylist(@PathVariable("playlistId") int playlistId) {
		return musicPlayerService.getAllMusicInPlaylist(playlistId);
	}

	@PostMapping("/delete/{musicId}")
	@ResponseBody
	public String deleteSong(@PathVariable("musicId") int musicId) {
		musicPlayerService.deleteMusicById(musicId);
		return "곡 삭제 완료";
	}

	@PostMapping("/updateOrder")
	@ResponseBody
	public String updateOrder(@RequestParam("order[]") List<Integer> order) {
		// 서비스에서 플레이리스트의 순서를 업데이트
		musicPlayerService.updatePlaylistOrder(order);
		return "순서 업데이트 완료";
	}

}
