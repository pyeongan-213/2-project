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

@Service
public class TopMenuSearchService {

	private static final String API_URL = "https://www.maniadb.com/api/search/%s/?sr=%s&key=example&v=0.5";

	// 아티스트와 앨범을 동시에 검색하는 메소드
	public SearchResult searchArtistAndAlbum(String query) {
		// 아티스트와 앨범에 대해 API 호출
		String artistXmlResult = callManiaDBApi(query, "artist");
		String albumXmlResult = callManiaDBApi(query, "album");

		// API 응답이 비어 있는지 확인
		if (artistXmlResult.isEmpty() && albumXmlResult.isEmpty()) {
			System.out.println("API 호출 실패 또는 응답이 비어 있습니다.");
			return new SearchResult(); // 빈 결과 반환
		}

		// 각각의 XML 결과를 파싱하여 아티스트와 앨범 리스트로 변환
		List<Artist> artistList = parseXmlToArtistList(artistXmlResult);
		List<Album> albumList = parseXmlToAlbumList(albumXmlResult);

		// 결과를 하나의 객체에 담아 반환
		return new SearchResult(artistList, albumList);
	}

	// API 호출 메소드 (예외 처리를 좀 더 구체적으로)
	private String callManiaDBApi(String query, String searchType) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format(API_URL, query, searchType);
		try {
			return restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ""; // 예외 발생 시 빈 문자열 반환
		}
	}

	// XML을 파싱하여 아티스트 리스트로 변환하는 메소드
	private List<Artist> parseXmlToArtistList(String xmlResult) {
		List<Artist> artistList = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(xmlResult, "", org.jsoup.parser.Parser.xmlParser());
			Elements items = doc.select("item");
			for (Element item : items) {
				String artistName = item.select("title").text();
				String period = item.select("period").text();
				String image = item.select("image").text();

				String majorsonglistRaw = item.select("maniadbmajorsonglist").text();
				List<String> majorSongList = Arrays.asList(majorsonglistRaw.split(" / "));

				String relatedartistlist = item.select("maniadbrelatedartistlist").text();
				String description = item.select("description").text();
				String link = item.select("link").text();

				// 아티스트 정보 설정
				Artist artist = new Artist();
				artist.setArtistName(artistName);
				artist.setPeriod(period);
				artist.setImage(image);
				artist.setMajorSongList(majorSongList);
				artist.setRelativDartistList(relatedartistlist);
				artist.setDescription(description);
				artist.setLink(link);

				artistList.add(artist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return artistList;
	}

	// XML을 파싱하여 앨범 리스트로 변환하는 메소드
	private List<Album> parseXmlToAlbumList(String xmlResult) {
		List<Album> albumList = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(xmlResult, "", org.jsoup.parser.Parser.xmlParser());
			Elements items = doc.select("item");
			for (Element item : items) {
				String albumArtist = item.select("maniadbartist name").text();
				String albumName = item.select("title").text();
				String releaseCompany = item.select("release_company").text();
				String albumimage = item.select("front image").text();
				String trackListRaw = item.select("maniadbtracklist").text();
				List<String> trackList = Arrays.asList(trackListRaw.split(" / "));
				String description = item.select("description").text();
				String guid = item.select("guid").text().replace("?s=0", "");

				Album album = new Album();
				album.setAlbumArtist(albumArtist);
				album.setAlbumName(albumName);
				album.setReleaseCompany(releaseCompany);
				album.setAlbumimage(albumimage);
				album.setTrackList(trackList);
				album.setDescription(description);
				album.setGuid(guid);

				albumList.add(album);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return albumList;
	}

	// 세부 정보 스크래핑 메소드
	public Object scrapeDetail(String guid, String type) {
		try {
			Document doc = Jsoup.connect(guid).get();
			if ("artist".equalsIgnoreCase(type)) {
				return scrapeArtistDetail(doc);
			} else {
				return scrapeAlbumDetail(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null; // 예외 발생 시 null 반환
		}
	}

	// 아티스트 세부 정보 스크래핑
	private ArtistDetail scrapeArtistDetail(Document doc) {
		ArtistDetail artistDetail = new ArtistDetail();
		artistDetail.setArtistName(doc.select("div.artist-name").first().text());
		artistDetail.setImage(doc.select("div#ARTIST_PHOTO a.highslide img").attr("src"));
		artistDetail.setPeriod(doc.select("td.artist-label:contains(ACTIVE) + td div").text());
		artistDetail.setDescription(doc.select("meta[property=og:description]").first().attr("content"));

		Elements imgElements = doc.select("div.text div a img");
		List<String> albumImageList = new ArrayList<>();
		int count = 0;
		for (Element img : imgElements) {
			albumImageList.add(img.attr("src"));
			if (++count >= 10)
				break; // 최대 10개까지만 가져오기
		}
		artistDetail.setAlbumImageList(albumImageList);

		Elements albumNameElements = doc.select("div[style='width:150px'] a");
		List<String> albumNameList = new ArrayList<>();
		count = 0;
		for (Element albumName : albumNameElements) {
			albumNameList.add(albumName.text());
			if (++count >= 10)
				break; // 최대 10개까지만 가져오기
		}
		artistDetail.setAlbumNameList(albumNameList);
		
		Elements albumGuidElements = doc.select("div[style='width:150px'] a");
		List<String> albumGuidList = new ArrayList<>();
		count = 0;
		for (Element albumGuid : albumGuidElements) {
			albumGuidList.add(albumGuid.attr("href"));
			if (++count >= 10)
				break; // 최대 10개까지만 가져오기
		}
		artistDetail.setAlbumGuidList(albumGuidList);
		return artistDetail;
	}

	// 앨범 세부 정보 스크래핑
	private AlbumDetail scrapeAlbumDetail(Document doc) {
		AlbumDetail albumDetail = new AlbumDetail();
		albumDetail.setAlbumName(doc.select("div.album-title").first().text());
		albumDetail.setArtistName(doc.select("div.album-artist a").text());
		albumDetail.setArtistGuid(doc.select("div.album-artist a").attr("href"));
		String description = doc.select("meta[property=og:description]").attr("content");
		if (description.length() > 1300) {
			description = description.substring(0, 1300); // 최대 1300자까지 자르기
		}
		albumDetail.setDescription(description);
		albumDetail.setImage(doc.select("div#body img").first().attr("src"));

		Elements trackElements = doc.select("table.album-tracks div.song a");
		List<String> trackList = new ArrayList<>();
		for (Element track : trackElements) {
			trackList.add(track.text());
			if (trackList.size() >= 30)
				break; // 최대 30개까지만 가져오기
		}
		albumDetail.setTrackList(trackList);

		List<String> runningTimeList = new ArrayList<>();
		Elements runningTimeElements = doc.select("td.runningtime");
		for (Element runningTime : runningTimeElements) {
			runningTimeList.add(runningTime.text());
			if (runningTimeList.size() >= 30)
				break; // 최대 30개까지만 가져오기
		}
		albumDetail.setRunningTimeList(runningTimeList);

		List<String> albumReleaseList = new ArrayList<>();
		Elements albumReleaseElements = doc.select("table.album-release td.release td");
		for (Element albumRelease : albumReleaseElements) {
			albumReleaseList.add(albumRelease.text());
			if (albumReleaseList.size() >= 4)
				break; // 최대 4개까지만 가져오기
		}
		albumDetail.setAlbumRelease(albumReleaseList);

		return albumDetail;
	}

	// 검색 결과를 담는 클래스
	public static class SearchResult {
		private List<Artist> artistList;
		private List<Album> albumList;

		public SearchResult() {
			this.artistList = new ArrayList<>();
			this.albumList = new ArrayList<>();
		}

		public SearchResult(List<Artist> artistList, List<Album> albumList) {
			this.artistList = artistList;
			this.albumList = albumList;
		}

		public List<Artist> getArtistList() {
			return artistList;
		}

		public List<Album> getAlbumList() {
			return albumList;
		}
	}

	// 아티스트와 앨범 클래스 정의 (생략된 부분 포함)
	public static class Artist {
		private String artistName;
		private String period;
		private String image;
		private List<String> majorSongList;
		private String relativDartistList;
		private String description;
		private String link;

		// Getters and setters
		public String getArtistName() {
			return artistName;
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

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public List<String> getMajorSongList() {
			return majorSongList;
		}

		public void setMajorSongList(List<String> majorSongList) {
			this.majorSongList = majorSongList;
		}

		public String getRelativDartistList() {
			return relativDartistList;
		}

		public void setRelativDartistList(String relativDartistList) {
			this.relativDartistList = relativDartistList;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}
	}

	public static class Album {
		private String albumName;
		private String albumArtist;
		private String releaseCompany;
		private List<String> trackList;
		private String albumimage;
		private String description;
		private String guid;

		// Getters and setters
		public String getAlbumName() {
			return albumName;
		}

		public void setAlbumName(String albumName) {
			this.albumName = albumName;
		}

		public String getAlbumArtist() {
			return albumArtist;
		}

		public void setAlbumArtist(String albumArtist) {
			this.albumArtist = albumArtist;
		}

		public String getReleaseCompany() {
			return releaseCompany;
		}

		public void setReleaseCompany(String releaseCompany) {
			this.releaseCompany = releaseCompany;
		}

		public List<String> getTrackList() {
			return trackList;
		}

		public void setTrackList(List<String> trackList) {
			this.trackList = trackList;
		}

		public String getAlbumimage() {
			return albumimage;
		}

		public void setAlbumimage(String albumimage) {
			this.albumimage = albumimage;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getGuid() {
			return guid;
		}

		public void setGuid(String guid) {
			this.guid = guid;
		}
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

		public List<String> getAlbumGuidList() {
			return albumGuidList;
		}

		public void setAlbumGuidList(List<String> albumGuidList) {
			this.albumGuidList = albumGuidList;
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
		private String artistGuid;
		private String albumName;
		private String period;
		private String description;
		private String image;
		private List<String> trackList;
		private List<String> runningTimeList;
		private List<String> albumRelease;

		// Getters and setters

		public String getArtistName() {
			return artistName;
		}

		public String getArtistGuid() {
			return artistGuid;
		}

		public void setArtistGuid(String artistGuid) {
			this.artistGuid = artistGuid;
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
