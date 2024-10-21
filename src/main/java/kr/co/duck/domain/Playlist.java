package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "playlist")
public class Playlist {

	@Id
	@Column(name = "playlist_id")
	private int playlist_id;

	@Column(name = "member_id")
	private int member_id;

	@Column(name = "playlistname")
	private String playlistname;

	@Column(name = "createDate")
	private String createDate;

	// Getter와 Setter
	public int getPlaylist_id() {
		return playlist_id;
	}

	public void setPlaylist_id(int playlist_id) {
		this.playlist_id = playlist_id;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getPlaylistname() {
		return playlistname;
	}

	public void setPlaylistname(String playlistname) {
		this.playlistname = playlistname;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Playlist{id=" + this.playlist_id + ", name='" + this.playlistname + "'}";
	}

}
