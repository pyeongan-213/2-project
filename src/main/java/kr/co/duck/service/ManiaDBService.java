package kr.co.duck.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import kr.co.duck.service.TopMenuSearchService.Album;
import kr.co.duck.service.TopMenuSearchService.Artist;

@Service
public class ManiaDBService {

	private static final String API_URL = "https://www.maniadb.com/api/search/%s/?sr=%s&key=example&v=0.5";

	public List<?> searchMusic(String query, String searchType) {
		// API 호출 및 결과 가져오기 (XML 형식으로)
		String xmlResult = callManiaDBApi(query, searchType);
		// XML 결과가 비어 있거나 처리할 수 없는 경우 처리
		if (xmlResult.isEmpty()) {
			System.out.println("API 호출 실패 또는 응답이 비어 있습니다: " + xmlResult);
			return new ArrayList<>(); // 빈 리스트 반환
		}

		// searchType에 따른 처리
		switch (searchType.toLowerCase()) {
		case "artist":
			return parseXmlToArtistList(xmlResult);
		case "album":
			return parseXmlToAlbumList(xmlResult);
		default:
			return parseXmlToAlbumList(xmlResult);
		}
	}

	public String RandomPage() {
		int num = ((int) (Math.random() * 12) + 1);
		System.out.println(num);
		if (num == 1) { 
			return "85680954";

		} else if (num == 2) { 
			return "85682814";

		} else if (num == 3) {
			return "85663989";

		} else if (num == 4) {
			return "85556176";

		} else if (num == 5) { 
			return "85399225";

		} else if (num == 6) {
			return "85323248";

		} else if (num == 7) {
			return "84729887";

		} else if (num == 8) { 
			return "83180213";

		} else if (num == 9) { 
			return "85075031";

		} else if (num == 10) { 
			return "81172042";

		} else if (num == 11) {
			return "84539814";

		} else if (num == 12) { 
			return "80673272";

		} 
		return "82614128";

	}

	private String callManiaDBApi(String query, String searchType) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format(API_URL, query, searchType);
		try {
			return restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			e.printStackTrace(); // 예외 출력
			return ""; // 예외 발생 시 빈 문자열 반환
		}
	}

	private List<Artist> parseXmlToArtistList(String xmlResult) {
		List<Artist> artistList = new ArrayList<>();
		try {
			// Jsoup을 사용하여 XML 파싱
			xmlResult = xmlResult.replaceAll(":", "");
			xmlResult = xmlResult.replaceAll("//", "://");
			Document doc = Jsoup.parse(xmlResult, "", org.jsoup.parser.Parser.xmlParser());
			// XML에서 item 요소를 선택
			Elements items = doc.select("item");
			for (Element item : items) {
				String artistName = item.select("title").text();
				String period = item.select("period").text();
				String image = item.select("image").text();
				String majorsonglistRaw = item.select("maniadbmajorsonglist").text();
				String relatedartistlist = item.select("maniadbrelatedartistlist").text();
				String description = item.select("description").text();
				String link = item.select("link").text();

				// 트랙 리스트를 분할하여 저장
				String[] majorsonglistArray = majorsonglistRaw.split(" / ");
				List<String> majorSongList = Arrays.asList(majorsonglistArray);

				// Artist 객체 생성 및 데이터 설정
				Artist artist = new Artist();
				artist.setArtistName(artistName);
				artist.setPeriod(period);
				artist.setImage(image);
				artist.setMajorSongList(majorSongList);
				artist.setRelativDartistList(relatedartistlist);
				artist.setDescription(description);
				artist.setLink(link);

				// 리스트에 추가
				artistList.add(artist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return artistList;
	}

	private List<Album> parseXmlToAlbumList(String xmlResult) {
		List<Album> albumList = new ArrayList<>();
		try {
			// Jsoup을 사용하여 XML 파싱
			xmlResult = xmlResult.replaceAll(":", "");
			xmlResult = xmlResult.replaceAll("//", "://");
			xmlResult = xmlResult.replace("[Disc 1]", "");
			xmlResult = xmlResult.replace("[Disc 2]", " / [Disc 2]");
			Document doc = Jsoup.parse(xmlResult, "", org.jsoup.parser.Parser.xmlParser());

			// XML에서 item 요소를 선택
			Elements items = doc.select("item");
			for (Element item : items) {
				String albumArtist = item.select("maniadbartist name").text();
				String albumName = item.select("title").text();
				String releaseCompany = item.select("release_company").text();
				String albumimage = item.select("front image").text();
				String trackListRaw = item.select("maniadbtracklist").text();
				String description = item.select("description").text();

				String guid = item.select("guid").text();

				guid = guid.replace("?s=0", "");

				// 트랙 리스트를 '/' 단위로 분할하여 배열로 저장
				String[] trackListArray = trackListRaw.split(" / ");
				List<String> trackList = Arrays.asList(trackListArray);

				// Album 객체 생성 및 데이터 설정
				Album album = new Album();
				album.setAlbumArtist(albumArtist);
				album.setAlbumName(albumName);
				album.setReleaseCompany(releaseCompany);
				album.setAlbumimage(albumimage);
				album.setTrackList(trackList);
				album.setDescription(description);
				album.setGuid(guid);

				// 리스트에 추가
				albumList.add(album);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return albumList;
	}

	public Object scrapeDetail(String guid, String type) {
		Object result = null;
		try {
			// guid에 있는 URL로 접속하여 데이터를 스크래핑
			Document doc = Jsoup.connect(guid).get();

			// type에 따라 다른 데이터를 스크래핑
			if ("artist".equalsIgnoreCase(type)) {
				result = scrapeArtistDetail(doc);
			} else {
				result = scrapeAlbumDetail(doc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private ArtistDetail scrapeArtistDetail(Document doc) {
		ArtistDetail artistDetail = new ArtistDetail();
		artistDetail.setArtistName(doc.select("div.artist-name").first().text());

		String artistImage = doc.select("div#ARTIST_PHOTO a.highslide img").attr("src");
		artistImage = artistImage.replace("_t/260", "");
		artistDetail.setImage(artistImage);

		// ACTIVE 데이터 가져오기
		artistDetail.setPeriod(doc.select("td.artist-label:contains(ACTIVE) + td div").text());

		// 설명 가져오기 (meta 태그)
		String description = doc.select("meta[property=og:description]").first().attr("content");
		if (description == null || description.isEmpty()) {
			description = "아티스트 세부정보가 없습니다";
		}
		artistDetail.setDescription(description);

		// 앨범 아트 이미지 리스트 가져오기
		Elements imgElements = doc.select("div.text div a img");
		List<String> albumImageList = new ArrayList<>();
		int count = 0;
		for (Element img : imgElements) {
			String albumImage = img.attr("src");
			albumImage = albumImage.replace("_t/150", "");
			albumImageList.add(albumImage);
			if (++count >= 10)
				break; // 최대 10개까지만 가져오기
		}
		artistDetail.setAlbumImageList(albumImageList);

		// 앨범 이름 리스트 가져오기
		Elements albumNameElements = doc.select("div[style='width:150px'] a");
		List<String> albumNameList = new ArrayList<>();
		count = 0;
		for (Element albumName : albumNameElements) {
			albumNameList.add(albumName.text());
			if (++count >= 10)
				break; // 최대 10개까지만 가져오기
		}
		artistDetail.setAlbumNameList(albumNameList);

		// 앨범 guid 가져오기; 연결용
		Elements guidElements = doc.select("div[style='width:150px'] a");
		List<String> albumGuidList = new ArrayList<>();
		count = 0;
		for (Element albumguid : guidElements) {
			String guidRaw = albumguid.attr("href");
			String guid = guidRaw.replace("/album/", "");
			albumGuidList.add("http://www.maniadb.com/album/" + guid);
			if (++count >= 10)
				break; // 최대 10개까지만 가져오기
		}
		artistDetail.setAlbumGuidList(albumGuidList);

		return artistDetail;
	}

	// Album 상세 정보 스크래핑
	private AlbumDetail scrapeAlbumDetail(Document doc) {
		AlbumDetail albumDetail = new AlbumDetail();
		albumDetail.setAlbumName(doc.select("div.album-title").first().text());
		albumDetail.setArtistName(doc.select("div.album-artist a").text());

		// 설명 가져오기 (meta 태그)
		String description = doc.select("meta[property=og:description]").attr("content");
		if (description == null || description.isEmpty()) {
			description = "앨범 세부정보가 없습니다";
		}
		if (description.length() > 1300) {
			description = description.substring(0, 1300); // 최대 200자까지 자르기
		}
		albumDetail.setDescription(description);
		String albumImage = doc.select("div#body img").first().attr("src");
		albumImage = albumImage.replace("_t/260", "");
		albumDetail.setImage(albumImage);
		// 트랙 리스트 가져오기
		Elements trackElements = doc.select("table.album-tracks div.song a");
		List<String> trackList = new ArrayList<String>();

		// 최대 30개의 src 속성 값 출력
		int count = 0;
		for (Element track : trackElements) {
			String src = track.text();
			trackList.add(src);
			count++;
			if (count >= 30)
				break; // 최대 30개까지만 가져오기
		}
		albumDetail.setTrackList(trackList);

		List<String> runningTimeList = new ArrayList<String>();
		Elements runningTimeElements = doc.select("td.runningtime");
		// 최대 30개의 src 속성 값 출력
		count = 0;
		for (Element runningTime : runningTimeElements) {
			String src = runningTime.text();
			runningTimeList.add(src);
			count++;
			if (count >= 30)
				break; // 최대 30개까지만 가져오기
		}

		albumDetail.setRunningTimeList(runningTimeList);

		List<String> albumReleaseList = new ArrayList<String>();
		Elements albumReleaseElements = doc.select("table.album-release td.release td");
		// 최대 30개의 src 속성 값 출력
		count = 0;
		for (Element albumRelease : albumReleaseElements) {
			String src = albumRelease.text();
			albumReleaseList.add(src);
			count++;
			if (count >= 4)
				break; // 최대 2개까지만 가져오기
		}

		albumDetail.setAlbumRelease(albumReleaseList);

		return albumDetail;
	}

	public ArtistDetail mainArtistCrawling(String artistName) {
		// artistName으로 ManiaDB에서 검색
		List<Artist> artistList = parseXmlToArtistList(callManiaDBApi(artistName, "artist"));

		// 검색 결과가 비어 있으면 null 반환
		if (artistList.isEmpty()) {
			return null;
		}

		// 첫 번째 아티스트의 link 값을 가져옴
		String artistLink = artistList.get(0).getLink();

		// 해당 링크로 아티스트 상세 정보를 스크래핑
		ArtistDetail artistDetail = (ArtistDetail) scrapeDetail(artistLink, "artist");

		return artistDetail;
	}

	public class ArtistDetail {
		private String artistName;
		private String period;
		private String description;
		private String image;
		private List<String> albumImageList;
		private List<String> albumNameList;
		private List<String> albumGuidList;
		private String debutDate;

		// Getters and setters
		public String getArtistName() {
			return artistName;
		}

		public String getDebutDate() {
			return debutDate;
		}

		public void setDebutDate(String debutDate) {
			this.debutDate = debutDate;
		}

		public void setArtistName(String artistName) {
			this.artistName = artistName;
		}

		public List<String> getAlbumGuidList() {
			return albumGuidList;
		}

		public void setAlbumGuidList(List<String> albumGuidList) {
			this.albumGuidList = albumGuidList;
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

		public List<String> getAlbumImageList() {
			return albumImageList;
		}

		public void setAlbumImageList(List<String> albumImageList) {
			this.albumImageList = albumImageList;
		}

		public List<String> getAlbumNameList() {
			return albumNameList;
		}

		public void setAlbumNameList(List<String> albumNameList) {
			this.albumNameList = albumNameList;
		}

	}

	public class AlbumDetail {
		private String artistName;
		private String albumName;
		private String period;
		private String description;
		private String image;
		private List<String> trackList;
		private List<String> runningTimeList;
		private List<String> albumRelease;

		public String getArtistName() {
			return artistName;
		}

		public List<String> getAlbumRelease() {
			return albumRelease;
		}

		public void setAlbumRelease(List<String> albumRelease) {
			this.albumRelease = albumRelease;
		}

		public List<String> getRunningTimeList() {
			return runningTimeList;
		}

		public void setRunningTimeList(List<String> runningTimeList) {
			this.runningTimeList = runningTimeList;
		}

		public List<String> getTrackList() {
			return trackList;
		}

		public void setTrackList(List<String> trackList) {
			this.trackList = trackList;
		}

		public String getAlbumName() {
			return albumName;
		}

		public void setAlbumName(String albumName) {
			this.albumName = albumName;
		}

		public void setArtistName(String artistName) {
			this.artistName = artistName;
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
	}
}
