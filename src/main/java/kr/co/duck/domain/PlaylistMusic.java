package kr.co.duck.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "playlist_music")
public class PlaylistMusic {

    @Id
    @Column(name = "playlist_id")
    private int playlistid;

    @Column(name = "music_id")
    private int musicid;

    @Column(name = "member_id")
    private int memberid;

    @Column(name = "playorder")
    private int playorder;

    // Getter와 Setter 추가
    public int getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(int playlistid) {
        this.playlistid = playlistid;
    }

    public int getMusicid() {
        return musicid;
    }

    public void setMusicid(int musicid) {
        this.musicid = musicid;
    }

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public int getPlayorder() {
        return playorder;
    }

    public void setPlayorder(int playorder) {
        this.playorder = playorder;
    }
}
