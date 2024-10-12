package kr.co.duck.beans;

// 기능: 프론트에서 받는 시그널링용 Message
public class WebSocketMessageBean {
	private String sender;
	private String type;
	private String data;
	private Long roomId;
	private String nickname;
	private String receiver;
	private Object offer;
	private Object answer;
	private Object candidate;
	private Object sdp;

	// 기본 생성자
	public WebSocketMessageBean() {
	}

	// Getter 메서드
	public String getSender() {
		return sender;
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

	public String getNickname() {
		return nickname;
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

	public void setType(String type) {
		this.type = type;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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
