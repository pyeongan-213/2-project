package kr.co.duck.service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ChartService {
    public static void main(String[] args) {
        String url = "https://www.melon.com/chart/index.htm";  // 멜론 차트 URL

        try {
            // URL에서 HTML 문서 가져오기
            Document doc = Jsoup.connect(url).get();

            // 노래 제목과 아티스트를 추출하는 Selector
            Elements songElements = doc.select("div.ellipsis.rank01 a"); // 노래 제목
            Elements artistElements = doc.select("div.ellipsis.rank02 a"); // 아티스트

            // 100위까지 반복
            for (int i = 0; i < 100; i++) {
                String songTitle = songElements.get(i).text();   // 노래 제목 추출
                String artist = artistElements.get(i).text();    // 아티스트 이름 추출

                // 콘솔에 출력
                System.out.println((i + 1) + "위: " + songTitle + " - " + artist);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}