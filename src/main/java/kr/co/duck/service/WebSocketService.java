package kr.co.duck.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import kr.co.duck.beans.WebSocketMessageBean;
import kr.co.duck.beans.WebSocketResponseBean;
import kr.co.duck.repository.SessionRepository;
import kr.co.duck.util.CustomException;
import kr.co.duck.util.StatusCode;

@Service
public class WebSocketService {

	private final SessionRepository sessionRepository;

	public WebSocketService(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	public void handleJoinRoom(WebSocketSession session, WebSocketMessageBean message, Long roomId, String userName) {
		if (sessionRepository.hasRoom(roomId)) {
			sessionRepository.addClient(roomId, session);
		} else {
			sessionRepository.addClientInNewRoom(roomId, session);
		}
		sessionRepository.saveRoomIdToSession(session, roomId);
		sessionRepository.addNicknameInRoom(session.getId(), message.getNickname());

		Map<String, WebSocketSession> joinClientList = sessionRepository.getClientList(roomId);
		List<String> exportSessionList = new ArrayList<>();
		Map<String, String> exportNicknameList = new HashMap<>();

		for (Map.Entry<String, WebSocketSession> entry : joinClientList.entrySet()) {
			if (entry.getValue() != session) {
				exportSessionList.add(entry.getKey());
				exportNicknameList.put(entry.getKey(), sessionRepository.getNicknameInRoom(entry.getKey()));
			}
		}

		sendMessage(session,
				new WebSocketResponseBean(userName, null, "all_users", message.getData(), roomId, exportSessionList,
						exportNicknameList, null, message.getOffer(), message.getAnswer(), message.getCandidate(),
						message.getSdp()));
	}

	public void handleOfferAnswerCandidate(WebSocketSession session, WebSocketMessageBean message, Long roomId) {
		if (sessionRepository.hasRoom(roomId)) {
			Map<String, WebSocketSession> clientList = sessionRepository.getClientList(roomId);

			if (clientList.containsKey(message.getReceiver())) {
				WebSocketSession ws = clientList.get(message.getReceiver());
				sendMessage(ws,
						new WebSocketResponseBean(session.getId(), message.getNickname(), message.getType(),
								message.getData(), roomId, null, null, message.getReceiver(), message.getOffer(),
								message.getAnswer(), message.getCandidate(), message.getSdp()));
			}
		} else {
			throw new CustomException(StatusCode.SESSION_ROOM_NOT_FOUND);
		}
	}

	public void handleDisconnect(WebSocketSession session) {
		String nickname = sessionRepository.getNicknameInRoom(session.getId());
		Long roomId = sessionRepository.getRoomId(session);
		sessionRepository.deleteClient(roomId, session);
		sessionRepository.deleteRoomIdToSession(session);
		sessionRepository.deleteNicknameInRoom(session.getId());

		Map<String, WebSocketSession> clientList = sessionRepository.getClientList(roomId);
		for (Map.Entry<String, WebSocketSession> oneClient : clientList.entrySet()) {
			sendMessage(oneClient.getValue(), new WebSocketResponseBean(session.getId(), null, "leave", null, roomId,
					null, null, oneClient.getKey(), null, null, null, null));
		}
	}

	private void sendMessage(WebSocketSession session, WebSocketResponseBean message) {
		// 메시지 발송 메서드 구현
	}
}
