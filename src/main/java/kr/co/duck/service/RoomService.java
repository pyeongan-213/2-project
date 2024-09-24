package kr.co.duck.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.duck.beans.RoomBean;

@Service
public class RoomService {
	private List<RoomBean> roomList = new ArrayList<>();

	// 방 생성
	public void createRoom(RoomBean room) {
		room.setRoomId(roomList.size() + 1); // 방 ID는 리스트 크기를 기준으로 자동 부여
		roomList.add(room);
	}

	// 방 목록 반환
	public List<RoomBean> getRoomList() {
		return new ArrayList<>(roomList); // 새로운 리스트로 반환하여 원본 보호
	}
}
