package kr.co.duck.controller;

import kr.co.duck.crawling.CrawlingAlbum;
import kr.co.duck.crawling.CrawlingArtist;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;

@Controller
public class ArtistAlbumController {

    @GetMapping("/artistAlbumInfo")
    public String getArtistAlbumInfo(Model model) {
        CrawlingArtist crawlingArtist = new CrawlingArtist();
        CrawlingAlbum crawlingAlbum = new CrawlingAlbum();

        try {
            // Melon 아티스트 정보 크롤링
            List<HashMap<String, String>> artistList = crawlingArtist.getArtistInfo();
            model.addAttribute("artistList", artistList);

            // Genie 앨범 정보 크롤링
            List<HashMap<String, String>> albumList = crawlingAlbum.getHomeNewAlbumPg1();
            model.addAttribute("albumList", albumList);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "정보를 가져오는 중 오류가 발생했습니다.");
        }

        return "artistAlbumView"; // JSP 파일 이름 반환 (artistAlbumView.jsp)
    }
}
