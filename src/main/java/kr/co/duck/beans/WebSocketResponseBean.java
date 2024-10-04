package kr.co.duck.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

// 기능: 프론트에 응답하는 시그널링용 Message
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketResponseBean {
	private String sender;
	private String senderNickName;
	private String type;
	private String data;
	private Long roomId;
	private List<String> allUsers;
	private Map<String, String> allUsersNickNames;
	private String receiver;
	private Object offer;
	private Object answer;
	private Object candidate;
	private Object sdp;

	// 기본 생성자
	public WebSocketResponseBean() {
	}

	// 모든 필드를 포함하는 생성자
	public WebSocketResponseBean(String sender, String senderNickName, String type, String data, Long roomId,
			List<String> allUsers, Map<String, String> allUsersNickNames, String receiver, Object offer, Object answer,
			Object candidate, Object sdp) {
		this.sender = sender;
		this.senderNickName = senderNickName;
		this.type = type;
		this.data = data;
		this.roomId = roomId;
		this.allUsers = allUsers;
		this.allUsersNickNames = allUsersNickNames;
		this.receiver = receiver;
		this.offer = offer;
		this.answer = answer;
		this.candidate = candidate;
		this.sdp = sdp;
	}

	// Getter 메서드
	public String getSender() {
		return sender;
	}

	public String getSenderNickName() {
		return senderNickName;
	}

	public String getType() {
		return type;
	}

	public String getData() {
		return data;
	}

	public Long getRoomId() {
		return roomId;
	}

	public List<String> getAllUsers() {
		return allUsers;
	}

	public Map<String, String> getAllUsersNickNames() {
		return allUsersNickNames;
	}

	public String getReceiver() {
		return receiver;
	}

	public Object getOffer() {
		return offer;
	}

	public Object getAnswer() {
		return answer;
	}

	public Object getCandidate() {
		return candidate;
	}

	public Object getSdp() {
		return sdp;
	}

	// Setter 메서드
	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSenderNickName(String senderNickName) {
		this.senderNickName = senderNickName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public void setAllUsers(List<String> allUsers) {
		this.allUsers = allUsers;
	}

	public void setAllUsersNickNames(Map<String, String> allUsersNickNames) {
		this.allUsersNickNames = allUsersNickNames;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setOffer(Object offer) {
		this.offer = offer;
	}

	public void setAnswer(Object answer) {
		this.answer = answer;
	}

	public void setCandidate(Object candidate) {
		this.candidate = candidate;
	}

	public void setSdp(Object sdp) {
		this.sdp = sdp;
	}
}
