package kr.co.duck.beans;

public class MusicBean {
	private String musicName; // 노래 제목
	private String artist; // 아티스트 (YouTube 채널 이름)
	private String musicLength; // 동영상 길이
	private String videoUrl; // YouTube 비디오 URL
	private String thumbnailUrl; // 비디오 썸네일 URL

	// 기본 생성자
	public MusicBean() {
	}

	// 모든 필드를 받는 생성자
	public MusicBean(String musicName, String artist, String musicLength, String videoUrl, String thumbnailUrl) {
		this.musicName = musicName;
		this.artist = artist;
		this.musicLength = musicLength;
		this.videoUrl = videoUrl;
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getMusicLength() {
		return musicLength;
	}

	public void setMusicLength(String musicLength) {
		this.musicLength = musicLength;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

}
