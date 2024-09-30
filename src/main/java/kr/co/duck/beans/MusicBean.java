package kr.co.duck.beans;

public class MusicBean {
    private int musicId;  // 음악 식별자 (MUSIC_ID)
    private String musicName;  // 제목 (Music_Name)
    private String artist;  // 아티스트 (Artist)
    private String genre;  // 장르 (Genre)
    private String music_Length;  // 노래 길이 (Music_Length)
    private String image;  // 이미지 (Image)
    private String video1;  // 비디오 URL 1 (Video1)
    private String video2;  // 비디오 URL 2 (Video2)
    private String video3;  // 비디오 URL 3 (Video3)

    // 기본 생성자
    public MusicBean() {}

    // 모든 필드를 포함하는 생성자
    public MusicBean(int musicId, String musicName, String artist, String genre, String musicLength, String image, String video1, String video2, String video3) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.artist = artist;
        this.genre = genre;
        this.music_Length = musicLength;
        this.image = image;
        this.video1 = video1;
        this.video2 = video2;
        this.video3 = video3;
    }

    // Getter 및 Setter 메서드
    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMusicLength() {
        return music_Length;
    }

    public void setMusicLength(String musicLength) {
        this.music_Length = musicLength;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo1() {
        return video1;
    }

    public void setVideo1(String video1) {
        this.video1 = video1;
    }

    public String getVideo2() {
        return video2;
    }

    public void setVideo2(String video2) {
        this.video2 = video2;
    }

    public String getVideo3() {
        return video3;
    }

    public void setVideo3(String video3) {
        this.video3 = video3;
    }
}
