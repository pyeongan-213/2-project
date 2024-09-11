package kr.co.duck.beans;

public class StreamingBean {

	private int streaming_id;
	private String play_record_start_time;
	private String play_record_end_time;
	private String streaming_playlist;
	private int streamer_id;
	private int join_user_id;
	private String notification;
	private String file_path;
	
	public int getStreaming_id() {
		return streaming_id;
	}
	public void setStreaming_id(int streaming_id) {
		this.streaming_id = streaming_id;
	}
	public String getPlay_record_start_time() {
		return play_record_start_time;
	}
	public void setPlay_record_start_time(String play_record_start_time) {
		this.play_record_start_time = play_record_start_time;
	}
	public String getPlay_record_end_time() {
		return play_record_end_time;
	}
	public void setPlay_record_end_time(String play_record_end_time) {
		this.play_record_end_time = play_record_end_time;
	}
	public String getStreaming_playlist() {
		return streaming_playlist;
	}
	public void setStreaming_playlist(String streaming_playlist) {
		this.streaming_playlist = streaming_playlist;
	}
	public int getStreamer_id() {
		return streamer_id;
	}
	public void setStreamer_id(int streamer_id) {
		this.streamer_id = streamer_id;
	}
	public int getJoin_user_id() {
		return join_user_id;
	}
	public void setJoin_user_id(int join_user_id) {
		this.join_user_id = join_user_id;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	
	
	
}
