package kr.co.duck.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// 기능: 웹소켓에 필요한 세션 정보를 저장, 관리
@Component
public class SessionRepository {

	// 세션 저장 1) 방 ID를 키로 하여 방마다 클라이언트 세션들을 저장
	private final Map<Long, Map<String, WebSocketSession>> clientsInRoom = new ConcurrentHashMap<>();

	// 세션 저장 2) 각 세션이 어느 방에 속해있는지를 저장
	private final Map<WebSocketSession, Long> roomIdToSession = new HashMap<>();

	// 세션 저장 3) 각 세션 ID와 닉네임을 저장
	private final Map<String, String> nicknamesInRoom = new HashMap<>();

	// 해당 방의 클라이언트 리스트 조회
	public Map<String, WebSocketSession> getClientList(Long roomId) {
		return clientsInRoom.get(roomId);
	}

	// 해당 방 존재 유무 조회
	public boolean hasRoom(Long roomId) {
		return clientsInRoom.containsKey(roomId);
	}

	// 해당 세션이 어느 방에 있는지 조회
	public Long getRoomId(WebSocketSession session) {
		return roomIdToSession.get(session);
	}

	// 새로운 방에 세션을 추가하여 저장
	public void addClientInNewRoom(Long roomId, WebSocketSession session) {
		Map<String, WebSocketSession> newClient = new HashMap<>();
		newClient.put(session.getId(), session);
		clientsInRoom.put(roomId, newClient);
	}

	// 특정 클라이언트 세션을 제거하고 다시 저장
	public void deleteClient(Long roomId, WebSocketSession session) {
		Map<String, WebSocketSession> clientList = clientsInRoom.get(roomId);
		clientList.remove(session.getId());
		// 제거 후 다시 저장
		clientsInRoom.put(roomId, clientList);
	}

	// 하나의 클라이언트 세션 정보 추가
	public void addClient(Long roomId, WebSocketSession session) {
		clientsInRoom.get(roomId).put(session.getId(), session);
	}

	// 방의 모든 클라이언트 세션 정보 삭제 (방 폭파 시 사용)
	public void deleteAllClientsInRoom(int roomId) {
		clientsInRoom.remove(roomId);
	}

	// 특정 방에 해당하는 모든 세션 조회
	public Map<WebSocketSession, Long> searchRoomIdToSessionList(Long roomId) {
		return roomIdToSession.entrySet().stream().filter(entry -> entry.getValue().equals(roomId))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	// 세션을 키로 사용하여 세션이 어느 방에 속해 있는지 저장
	public void saveRoomIdToSession(WebSocketSession session, Long roomId) {
		roomIdToSession.put(session, roomId);
	}

	// 세션을 키로 사용하여 해당 세션 정보 삭제
	public void deleteRoomIdToSession(WebSocketSession session) {
		roomIdToSession.remove(session);
	}

	// 세션 ID로 닉네임 정보 조회
	public String getNicknameInRoom(String sessionId) {
		return this.nicknamesInRoom.get(sessionId);
	}

	// 세션 ID를 키로 닉네임 정보 저장
	public void addNicknameInRoom(String sessionId, String nickname) {
		this.nicknamesInRoom.put(sessionId, nickname);
	}

	// 세션 ID를 키로 닉네임 정보 삭제
	public void deleteNicknameInRoom(String sessionId) {
		this.nicknamesInRoom.remove(sessionId);
	}
}
