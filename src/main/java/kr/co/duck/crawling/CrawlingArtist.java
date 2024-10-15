package kr.co.duck.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlingArtist {

    private static String url = "https://www.melon.com/artistplus/artistchart/index.htm";
    private static String imgSuffixToRemove = "/melon/resize/104/quality/80/optimize";

    public List<HashMap<String, String>> getArtistInfo() throws IOException {
        List<HashMap<String, String>> artistInfoList = new ArrayList<>();

        Document document = Jsoup.connect(url)
                                 .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                                 .get();

        //System.out.println("Successfully connected to URL");

        // 아티스트 리스트를 포함한 요소 선택
        Elements artistElements = document.select("li.artistplus_li");

        int count = 0;
        for (Element artistElement : artistElements) {
            // 아티스트 이름 추출
            Element nameElement = artistElement.selectFirst("div.wrap_info a.ellipsis");
            String artistName = nameElement != null ? nameElement.text() : "";

            // 이미지 URL 추출
            Element imgElement = artistElement.selectFirst("div.wrap_thumb img");
            String imgUrl = imgElement != null ? imgElement.attr("src") : "";

            // 불필요한 문자열 제거
            if (imgUrl.contains(imgSuffixToRemove)) {
                imgUrl = imgUrl.replace(imgSuffixToRemove, "");
            }

            // HashMap에 아티스트 정보 추가
            if (!imgUrl.isEmpty() && !artistName.isEmpty()) {
                HashMap<String, String> artistInfo = new HashMap<>();
                artistInfo.put("artist", artistName);
                artistInfo.put("img", imgUrl);
                
                artistInfoList.add(artistInfo);
                count++;
                if (count >= 12) break; // 12개까지만 크롤링
            }
        }

        return artistInfoList;
    }

    public static void main(String[] args) {
        CrawlingArtist crawlingArtist = new CrawlingArtist();
        try {
            List<HashMap<String, String>> artists = crawlingArtist.getArtistInfo();
            for (HashMap<String, String> artist : artists) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
