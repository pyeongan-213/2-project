package kr.co.duck.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class CrawlingAlbumDataMelon {

	private static String url = "https://www.melon.com/album/detail.htm?albumId=%s";

	public AlbumDetail getAlbumDetail(String album_id) throws IOException {
        // AlbumDetail 객체 생성
        AlbumDetail albumDetail = new AlbumDetail();

        // URL 설정
        String albumUrl = String.format(url, album_id);

        // Jsoup으로 HTML 문서 가져오기
        Document doc = Jsoup.connect(albumUrl).get();

        // 앨범 기본 정보 크롤링
        Element photoZone = doc.selectFirst("a.image_typeAll > img");
        
        // Null 체크 후 데이터 추출
        if (photoZone != null) {
            String img_src = photoZone.attr("src");
            albumDetail.setImage(img_src);
        } else {
            albumDetail.setImage("이미지 없음");
        }

        
        String artistName = doc.selectFirst("div.artist a.artist_name").attr("title");
        albumDetail.setArtistName(artistName);
        
        String title = doc.selectFirst("div.song_name").text();
        albumDetail.setAlbumName(title);

		String releaseDate = doc.selectFirst("div.entry div.meta div.list dd").text();
		albumDetail.setPeriod(releaseDate);
        
        
     
        String description = doc.select("div.dtl_albuminfo.on div").text();
		if (description.length() > 1300) {
		    description = description.substring(0, 1300); // 최대 1300자까지 자르기
		}
		albumDetail.setDescription(description);
		
        
        
        // 곡 목록 및 러닝 타임 크롤링
        List<String> trackList = new ArrayList<>();
        List<String> runningTimeList = new ArrayList<>();
        Random random = new Random();
        Elements songList = doc.select("div.wrap_song_info");

        for (Element song : songList) {
            String songTitle = song.selectFirst("div.ellipsis span a").text();
            int randomNumber = random.nextInt(59)+1;
            String RNString = String.valueOf(randomNumber);
            String songTime = "3:" + RNString ; // 시간을 받아올 수 없음 : 랜덤한 시간과 숫자를 설정.

            trackList.add(songTitle);
            runningTimeList.add(songTime);
        }

        albumDetail.setTrackList(trackList);
        albumDetail.setRunningTimeList(runningTimeList);

        return albumDetail;
    }

	// AlbumDetail 클래스 정의 (기존과 동일)
	public static class AlbumDetail {
		private String artistName;
		private String albumName;
		private String period;
		private String description;
		private String image;
		private List<String> trackList;
		private List<String> runningTimeList;
		private List<String> albumRelease;

		// Getter, Setter
		public String getArtistName() {
			return artistName;
		}

		public void setArtistName(String artistName) {
			this.artistName = artistName;
		}

		public String getAlbumName() {
			return albumName;
		}

		public void setAlbumName(String albumName) {
			this.albumName = albumName;
		}

		public String getPeriod() {
			return period;
		}

		public void setPeriod(String period) {
			this.period = period;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public List<String> getTrackList() {
			return trackList;
		}

		public void setTrackList(List<String> trackList) {
			this.trackList = trackList;
		}

		public List<String> getRunningTimeList() {
			return runningTimeList;
		}

		public void setRunningTimeList(List<String> runningTimeList) {
			this.runningTimeList = runningTimeList;
		}

		public List<String> getAlbumRelease() {
			return albumRelease;
		}

		public void setAlbumRelease(List<String> albumRelease) {
			this.albumRelease = albumRelease;
		}
	}
}
