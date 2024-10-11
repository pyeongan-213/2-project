package kr.co.duck.beans;

public class PlaylistBean {
    private int playlist_Id;
    private String playlistName;
    private int memberId;
    
    // getter/setter
	public int getPlaylistId() {
		return playlist_Id;
	}
	public void setPlaylistId(int playlistId) {
		this.playlist_Id = playlistId;
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
    
}