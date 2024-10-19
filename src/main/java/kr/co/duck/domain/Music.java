package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="music")
public class Music {

	@Id
	@Column(name="music_id")
	private int music_Id;
	
	@Column(name="music_name")
	private String music_Name;
	
	@Column(name="artist")
	private String artist;
	
	@Column(name="music_length")
	private String musicLength;
	
	@Column(name="videourl")
	private String videoUrl;
	
	@Column(name="thumbnailurl")
	private String thumbNailUrl;
	public int getMusic_Id() {
		return music_Id;
	}
	
	
	
	public void setMusic_Id(int music_Id) {
		this.music_Id = music_Id;
	}
	public String getMusic_Name() {
		return music_Name;
	}
	public void setMusic_Name(String music_Name) {
		this.music_Name = music_Name;
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
	public String getThumbNailUrl() {
		return thumbNailUrl;
	}
	public void setThumbNailUrl(String thumbNailUrl) {
		this.thumbNailUrl = thumbNailUrl;
	}
	
	
	
}
