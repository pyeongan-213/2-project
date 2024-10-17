package kr.co.duck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
