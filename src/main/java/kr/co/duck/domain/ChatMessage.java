package kr.co.duck.domain; // 프로젝트의 패키지 경로에 맞게 설정

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 기능 : 프론트엔드와 서버 간 WebSocket 메시지 전송을 위한 DTO.
 * @JsonInclude(JsonInclude.Include.NON_NULL) 
 *  - Null이 아닌 필드만 포함하도록 설정.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage<T> {

    // 메시지 타입을 정의하는 열거형(Enum)
    public enum MessageType {
        TALK, ENTER, OFFER, ICE, ANSWER, CAMERAOFF
    }

    private String type;       // 메시지 타입
    private String roomId;     // 방 번호
    private String sender;     // 메시지 보낸 사람
    private T message;    // 메시지 내용
    private String nickname;   // 닉네임

    // 시그널링용 필드 (WebRTC 연결 처리에 사용)
    private String offer;      // WebRTC Offer
    private String ice;        // ICE 후보
    private String candidate;  // WebRTC Candidate
    private String answer;     // WebRTC Answer

    // 기본 생성자
    public ChatMessage() {
    }

  


	public ChatMessage(String type, String roomId, String sender, T message, String nickname, String offer, String ice,
			String candidate, String answer) {
		super();
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.nickname = nickname;
		this.offer = offer;
		this.ice = ice;
		this.candidate = candidate;
		this.answer = answer;
	}




	public ChatMessage(String sender, T message) {
        this.sender = sender;
        this.message = message;
    }
    
    // Getter와 Setter 메서드
    
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
