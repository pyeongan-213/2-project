package kr.co.duck.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "playlist_music")
public class Playlist_Music {

    @Id
    @Column(name = "playlist_id")
    private int playlist_id;
    

    @Column(name = "music_id")
    private int music_id;

    @Column(name = "member_id")
    private int member_id;

    @Column(name = "playorder")
    private int playorder;

    // Getter와 Setter 추가
    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getPlayorder() {
        return playorder;
    }

    public void setPlayorder(int playorder) {
        this.playorder = playorder;
    }
}
