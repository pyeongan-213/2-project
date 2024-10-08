package kr.co.duck.beans;

public class MusicBean {
    private int musicId; // MUSIC_id: 테이블의 기본 키
    private String musicName; // music_Name: 노래 제목
    private String artist; // artist: 아티스트 이름

    private String videoUrl; // videoUrl: 유튜브 비디오 URL
    private String thumbnailUrl; // thumbnailUrl: 비디오 썸네일 URL

    // 기본 생성자
    public MusicBean() {
    }

    // 모든 필드를 초기화하는 생성자
    public MusicBean(int musicID, String musicName, String artist, String musicLength, String videoUrl, String thumbnailUrl) {
        this.musicId = musicID;
        this.musicName = musicName;
        this.artist = artist;

        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    // 음악 ID (MUSIC_id)
    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicID) {
        this.musicId = musicID;
    }

    // 노래 제목 (music_Name)
    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    // 아티스트 이름 (artist)
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    // 유튜브 비디오 URL (videoUrl)
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    // 비디오 썸네일 URL (thumbnailUrl)
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
