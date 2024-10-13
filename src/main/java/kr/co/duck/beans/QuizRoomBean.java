package kr.co.duck.beans;

public class QuizRoomBean {
    private int quizRoomId;
    private String quizRoomName;
    private String quizRoomPassword;
    private String owner;
    private int status;
    private int memberCount;
    private int maxCapacity;
    private int maxMusic;
    private String quizQuestionType; // 퀴즈 질문 유형

    // 기본 생성자: 방 생성 시 멤버 카운트를 0으로 초기화
    public QuizRoomBean() {
        this.memberCount = 0; // 생성 시 인원 수를 0으로 초기화
    }

    // 모든 필드를 포함하는 생성자
    public QuizRoomBean(int quizRoomId, String quizRoomName, String quizRoomPassword, String owner, int status,
            int maxCapacity, int maxMusic, int memberCount, String quizQuestionType) {
        this.quizRoomId = quizRoomId;
        this.quizRoomName = quizRoomName;
        this.quizRoomPassword = quizRoomPassword;
        this.owner = owner;
        this.status = status;
        this.memberCount = 0; // 방 생성 시 멤버 카운트 0으로 설정
        this.maxCapacity = maxCapacity;
        this.quizQuestionType = quizQuestionType; // 퀴즈 질문 유형 추가
        this.maxMusic = maxMusic;
    }

    // Getter와 Setter 메서드

    public int getQuizRoomId() {
        return quizRoomId;
    }

    public void setQuizRoomId(int quizRoomId) {
        this.quizRoomId = quizRoomId;
    }

    public String getQuizRoomName() {
        return quizRoomName;
    }

    public void setQuizRoomName(String quizRoomName) {
        this.quizRoomName = quizRoomName;
    }

    public String getQuizRoomPassword() {
        return quizRoomPassword;
    }

    public void setQuizRoomPassword(String quizRoomPassword) {
        this.quizRoomPassword = quizRoomPassword;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxMusic() {
        return maxMusic;
    }

    public void setMaxMusic(int maxMusic) {
        this.maxMusic = maxMusic;
    }

    public String getQuizQuestionType() {
        return quizQuestionType;
    }

    public void setQuizQuestionType(String quizQuestionType) {
        this.quizQuestionType = quizQuestionType;
    }
}
