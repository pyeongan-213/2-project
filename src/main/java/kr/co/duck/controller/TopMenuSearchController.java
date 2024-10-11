package kr.co.duck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.service.TopMenuSearchService;
import kr.co.duck.service.TopMenuSearchService.SearchResult;

@Controller
public class TopMenuSearchController {

    @Autowired
    private TopMenuSearchService topMenuSearchService;
    
    // 검색 화면을 표시하는 매핑
    @GetMapping("/search/searchPage")
    public String showMainSearchRequestPage() {
        return "search/searchPage"; // JSP 파일의 위치
    }
    
    // 검색 요청을 처리하는 매핑
    @GetMapping("/search/TopMenuSearch")
    public String searchManiaDB(@RequestParam("query") String query,
            @RequestParam(value = "searchType", defaultValue = "artist") String searchType, Model model) {

        // searchType에 따라 ManiaDB에서 아티스트와 앨범 정보 검색
        SearchResult searchResult = topMenuSearchService.searchArtistAndAlbum(query);

        // 검색 결과를 모델에 추가
        model.addAttribute("artistList", searchResult.getArtistList());
        model.addAttribute("albumList", searchResult.getAlbumList());

        // 검색 결과를 포함한 페이지를 다시 렌더링
        return "search/searchPage"; // JSP 파일의 위치
    }
    
    // 상세페이지로 넘어가는 요청을 처리하는 매핑
    @GetMapping("search/parseDetail")
    public String parseDetail(@RequestParam("guid") String guid, @RequestParam("type") String type, Model model) {
        try {
            // Jsoup을 사용해 URL에 있는 정보를 파싱
            Object result = topMenuSearchService.scrapeDetail(guid, type);
            
            System.out.println(guid);
            System.out.println(type);
            
            // 모델에 데이터를 추가
            model.addAttribute("result", result);

            // 각 type에 따라 적절한 JSP로 이동
            if ("artist".equalsIgnoreCase(type)) {
                return "search/searchArtistInfo";
            } else {
                return "search/searchAlbumInfo";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "search/searchPage"; // 예외 발생 시 에러 페이지로 이동
        }
    }
}
