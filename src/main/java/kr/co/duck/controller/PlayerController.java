package kr.co.duck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.beans.MusicBean;
import kr.co.duck.beans.PlaylistBean;
import kr.co.duck.dao.MusicDAO;
import kr.co.duck.dao.PlaylistDAO;

@Controller
public class PlayerController {

    @Autowired
    private PlaylistDAO playlistDAO;

    @Autowired
    private MusicDAO musicDAO;

    // 모든 플레이리스트를 보여줌
    @GetMapping("/playlists")
    public String showAllPlaylists(Model model, @SessionAttribute("loginMemberBean") MemberBean loginMember) {
        int memberId = loginMember.getMember_id();
        List<PlaylistBean> playlists = playlistDAO.getPlaylistsByMemberId(memberId);
        model.addAttribute("playlists", playlists);
        return "playlist";  // playlist.jsp 파일을 렌더링
    }

    // 특정 플레이리스트에 속한 음악 목록을 보여줌
    @GetMapping("/playlist/{playlistId}")
    public String showPlaylist(@PathVariable int playlistId, Model model) {
        List<MusicBean> musicList = playlistDAO.getMusicListByPlaylistId(playlistId);
        model.addAttribute("musicList", musicList);
        return "playlist";  // 같은 JSP 파일에서 음악 목록을 동적으로 표시
    }
}
