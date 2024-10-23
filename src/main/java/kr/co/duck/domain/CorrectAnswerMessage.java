package kr.co.duck.domain;

public class CorrectAnswerMessage {
    private String sender;  // 정답자 닉네임
    private String songName;  // 맞춘 노래 제목
    private int timer;  // 타이머 정보 추가

    // 기본 생성자
    public CorrectAnswerMessage() {}

   
    public CorrectAnswerMessage(String sender, String songName, int timer) {
		super();
		this.sender = sender;
		this.songName = songName;
		this.timer = timer;
	}


	// Getter와 Setter
    
    
    public String getSender() {
        return sender;
    }

    public int getTimer() {
		return timer;
	}


	public void setTimer(int timer) {
		this.timer = timer;
	}


	public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
