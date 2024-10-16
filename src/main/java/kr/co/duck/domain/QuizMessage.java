package kr.co.duck.domain;

/**
 * 기능: 퀴즈 룸 채팅에 적용되는 메시지용 DTO
 */
public class QuizMessage<T> {

    private int quizRoomId;   // 퀴즈 방 식별자
    private String senderId;  // 발신자 ID
    private String sender;    // 발신자 이름
    private String nickname;  // 닉네임
    private T content;        // 메시지 내용
    private MessageType type; // 메시지 타입

    // 기본 생성자
    public QuizMessage() {
    }

    // 필요한 필드만 포함하는 생성자 (간편하게 사용 가능)
    public QuizMessage(String sender, T content) {
        this.sender = sender;
        this.content = content;
    }

    // 모든 필드를 포함한 생성자
    public QuizMessage(int quizRoomId, String senderId, String sender, String nickname, T content, MessageType type) {
        this.quizRoomId = quizRoomId;
        this.senderId = senderId;
        this.sender = sender;
        this.nickname = nickname;
        this.content = content;
        this.type = type;
    }

    // Getter 및 Setter 메서드
    public int getQuizRoomId() {
        return quizRoomId;
    }

    public void setQuizRoomId(int quizRoomId) {
        this.quizRoomId = quizRoomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    /**
     * 메시지 타입 열거형: 다양한 퀴즈 상태와 액션을 정의
     */
    public enum MessageType {
        JOIN, OWNER, ENTER, RULE, READY, START, QUESTION, SPOTLIGHT, FAIL, 
        SKIP, SUCCESS, WINNER, ENDQUIZ, FORCEDENDQUIZ, LEAVE, NEWOWNER, 
        END, REWARD, HINT, ANSWER, CORRECT, INCORRECT, NEXT
    }
}
