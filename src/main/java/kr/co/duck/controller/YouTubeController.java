package kr.co.duck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.service.YouTubeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.service.YouTubeService;

@Controller
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @PostMapping("/youtubeSearch")
    public String searchYouTube(@RequestParam("query") String query, Model model) {
        List<MusicBean> results = youTubeService.searchYouTube(query);
        model.addAttribute("searchResults", results);
        return "playlist/youtubeSearch";  // 검색 결과를 보여줄 JSP 파일로 이동
    }
}