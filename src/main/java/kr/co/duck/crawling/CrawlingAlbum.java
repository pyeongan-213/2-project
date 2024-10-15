package kr.co.duck.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlingAlbum {

	
	private static String url = "https://www.genie.co.kr/";

    public List<Element> base() throws IOException {
        List<Element> info = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div.slide-wrapper.active ul.bxslider li");

        for (Element element : elements) {
            Elements select = element.select("ul.list-album li");
            for (Element element1 : select) {
                info.add(element1);
            }
        }

        return info;
    }

    public List<HashMap<String, String>> getHomeNewAlbumPg1() throws IOException {
        List<HashMap<String, String>> pg1_info = new ArrayList<>();
        List<Element> pg1 = new ArrayList<>();
        List<Element> base = base();

        for (int i = 0; i < 12; i++) {
            pg1.add(base.get(i));
        }

        for (Element e : pg1) {
            String album_id = e.attr("album_id");
            //System.out.println(album_id);
            String img_src = e.selectFirst("div.cover img").attr("src");
            String img = "https:" + img_src;
            String title = e.selectFirst("div.info-album a.album-title").text();
            String artist = e.selectFirst("div.info-album a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("album_id", album_id);

            pg1_info.add(map);
        }

        return pg1_info;
    }
}
