package kr.co.duck.beans;

public class ChatBean {

    private int chat_id;  // 채팅 인덱스번호
    private int room_id;  // 방 ID
    private int member_id;  // 유저 식별번호 (DB에서 MEMBER_ID)
    private String chat_text;  // 채팅 내용
    private String chat_time;  // 채팅 시간 (VARCHAR 형식으로 저장)

    public ChatBean() {}
    
    public ChatBean(int chat_id, int room_id, int member_id, String chat_text, String chat_time) {
		super();
		this.chat_id = chat_id;
		this.room_id = room_id;
		this.member_id = member_id;
		this.chat_text = chat_text;
		this.chat_time = chat_time;
	}



	public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getChat_text() {
        return chat_text;
    }

    public void setChat_text(String chat_text) {
        this.chat_text = chat_text;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

	@Override
	public String toString() {
		return "ChatBean [chat_id=" + chat_id + ", room_id=" + room_id + ", member_id=" + member_id + ", chat_text="
				+ chat_text + ", chat_time=" + chat_time + "]";
	}
    
    
}
