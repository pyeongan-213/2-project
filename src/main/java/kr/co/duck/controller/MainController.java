package kr.co.duck.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.duck.crawling.CrawlingAlbum;
import kr.co.duck.crawling.CrawlingArtist;
import kr.co.duck.repository.KaokaopayInfoRepository;
import kr.co.duck.beans.MemberBean;

@Controller
public class MainController {

    @Autowired
    private KaokaopayInfoRepository kaokaopayInfoRepository;

    @GetMapping("/main")
    public String getMainPage(HttpSession session, Model model) {
        CrawlingArtist crawlingArtist = new CrawlingArtist();
        CrawlingAlbum crawlingAlbum = new CrawlingAlbum();

        try {
            // Melon 아티스트 정보 크롤링
            List<HashMap<String, String>> artistList = crawlingArtist.getArtistInfo();
            model.addAttribute("artistList", artistList);

            // Genie 앨범 정보 크롤링
            List<HashMap<String, String>> albumList = crawlingAlbum.getHomeNewAlbumPg1();
            model.addAttribute("albumList", albumList);

            // 세션에서 loginMemberBean을 가져옴
            MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

            // 로그인 여부 확인
            if (loginMemberBean != null && loginMemberBean.isMemberLogin()) {
                // 로그인 된 상태에서만 구독 여부 확인
                int member_id = loginMemberBean.getMember_id(); // 회원 ID 가져옴
                String subscriptionSid = kaokaopayInfoRepository.checkSubscribe(member_id);
                boolean isPremiumMember = (subscriptionSid != null);
                model.addAttribute("isPremiumMember", isPremiumMember);
            } else {
                // 로그인하지 않은 사용자일 경우 프리미엄 멤버가 아님
                model.addAttribute("isPremiumMember", false);
            }

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "정보를 가져오는 중 오류가 발생했습니다.");
        }

        return "main"; // main.jsp로 이동
    }
}
