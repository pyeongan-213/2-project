package kr.co.duck.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.service.ManiaDBService;

@Controller
public class ManiaDBController {

    @Autowired
    private ManiaDBService maniaDBService;

    @GetMapping("/temp/maniadbSearch")
    public String search(@RequestParam(value = "query", required = false, defaultValue = "") String query, Model model) {
        List<ManiaDBService.Music> result = new ArrayList<>();
        
        // query가 비어있지 않은 경우에만 API 호출
        if (!query.trim().isEmpty()) {
            result = maniaDBService.searchMusic(query);
        } else {
            model.addAttribute("resultMessage", "검색어를 입력하세요."); // 결과가 없을 때 메시지
        }

        model.addAttribute("result", result); // 결과 리스트 추가
        model.addAttribute("query", query); // 쿼리 파라미터를 모델에 추가
        return "temp/maniadbSearch";  // JSP 파일 경로
    }
}