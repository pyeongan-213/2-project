package kr.co.duck.service;

import kr.co.duck.beans.MusicBean;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

	// 플레이리스트를 저장할 리스트
	private List<MusicBean> playlist = new ArrayList<>();

	// 기본 생성자: 예시로 초기 플레이리스트 데이터 추가
	public PlaylistService() {
		playlist.add(new MusicBean(1, "Song Title 1", "Artist 1", "Pop", "3:30", "image1.jpg", "video1_url",
				"video2_url", "video3_url"));
		playlist.add(new MusicBean(2, "Song Title 2", "Artist 2", "Rock", "4:15", "image2.jpg", "video1_url",
				"video2_url", "video3_url"));
		playlist.add(new MusicBean(3, "Song Title 3", "Artist 3", "Jazz", "2:45", "image3.jpg", "video1_url",
				"video2_url", "video3_url"));
	}

	// 전체 플레이리스트를 반환하는 메서드
	public List<MusicBean> getPlaylist() {
		return playlist;
	}

	// 새로운 곡을 플레이리스트에 추가하는 메서드
	public void addSong(MusicBean song) {
		playlist.add(song);
	}

	// 곡을 제목으로 검색하여 플레이리스트에서 삭제하는 메서드
	public boolean removeSongByTitle(String title) {
		Optional<MusicBean> songToRemove = playlist.stream().filter(song -> song.getMusicName().equalsIgnoreCase(title))
				.findFirst();

		if (songToRemove.isPresent()) {
			playlist.remove(songToRemove.get());
			return true; // 삭제 성공
		} else {
			return false; // 곡을 찾을 수 없음
		}
	}

	// 곡 ID로 삭제하는 메서드
	public boolean removeSongById(int musicId) {
		Optional<MusicBean> songToRemove = playlist.stream().filter(song -> song.getMusicId() == musicId).findFirst();

		if (songToRemove.isPresent()) {
			playlist.remove(songToRemove.get());
			return true; // 삭제 성공
		} else {
			return false; // 곡을 찾을 수 없음
		}
	}

	// 곡을 재정렬하거나 순서를 변경할 수 있는 메서드
	public void updatePlaylistOrder(List<MusicBean> newOrder) {
		this.playlist = newOrder;
	}

	// 제목을 기준으로 곡을 검색하는 메서드
	public MusicBean findSongByTitle(String title) {
		return playlist.stream().filter(song -> song.getMusicName().equalsIgnoreCase(title)).findFirst().orElse(null);
	}

	// 곡 ID로 곡을 찾는 메서드
	public MusicBean findSongById(int musicId) {
		return playlist.stream().filter(song -> song.getMusicId() == musicId).findFirst().orElse(null);
	}

	// 플레이리스트에 이미 추가된 곡인지 확인하는 메서드
	public boolean isSongInPlaylist(String title) {
		return playlist.stream().anyMatch(song -> song.getMusicName().equalsIgnoreCase(title));
	}
}
