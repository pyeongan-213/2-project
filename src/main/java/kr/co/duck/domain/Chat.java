package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CHAT")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
	@SequenceGenerator(name = "chat_seq", sequenceName = "CHAT_SEQ", allocationSize = 1)
	private int chat_Id;

	@Column(name = "ROOM_ID", nullable = false)
	private int room_Id;

	@Column(name = "MEMBER_ID", nullable = false)
	private int member_Id;

	@Column(name = "CHAT_TEXT", nullable = false)
	private String chat_Text;

	@Column(name = "CHAT_TIME", nullable = false)
	private String chat_Time;

	public Chat() {

	}

	
	public Chat(int chat_Id, int room_Id, int member_Id, String chat_Text, String chat_Time) {
		super();
		this.chat_Id = chat_Id;
		this.room_Id = room_Id;
		this.member_Id = member_Id;
		this.chat_Text = chat_Text;
		this.chat_Time = chat_Time;
	}

	public int getChat_Id() {
		return chat_Id;
	}

	public void setChat_Id(int chat_Id) {
		this.chat_Id = chat_Id;
	}

	public int getRoom_Id() {
		return room_Id;
	}

	public void setRoom_Id(int room_Id) {
		this.room_Id = room_Id;
	}

	public int getMember_Id() {
		return member_Id;
	}

	public void setMember_Id(int member_Id) {
		this.member_Id = member_Id;
	}

	public String getChat_Text() {
		return chat_Text;
	}

	public void setChat_Text(String chat_Text) {
		this.chat_Text = chat_Text;
	}

	public String getChat_Time() {
		return chat_Time;
	}

	public void setChat_Time(String chat_Time) {
		this.chat_Time = chat_Time;
	}

}