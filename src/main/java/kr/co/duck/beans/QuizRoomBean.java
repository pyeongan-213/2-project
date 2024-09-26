package kr.co.duck.beans;

import java.util.List;

public class QuizRoomBean {
	private int id;
	private String room_name;
	private String room_password;
	private String owner;
	private boolean status;
	private int member_count;
	private List<MemberBean> members;

	// 기본 생성자
	public QuizRoomBean() {
	}

	// 모든 필드를 포함하는 생성자
	public QuizRoomBean(int id, String room_name, String room_password, String owner, boolean status, int member_count,
			List<MemberBean> members) {
		this.id = id;
		this.room_name = room_name;
		this.room_password = room_password;
		this.owner = owner;
		this.status = status;
		this.member_count = member_count;
		this.members = members;
	}

	// Getter와 Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public String getRoom_password() {
		return room_password;
	}

	public void setRoom_password(String room_password) {
		this.room_password = room_password;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getMember_count() {
		return member_count;
	}

	public void setMember_count(int member_count) {
		this.member_count = member_count;
	}

	public List<MemberBean> getMembers() {
		return members;
	}

	public void setMembers(List<MemberBean> members) {
		this.members = members;
	}

	@Override
	public String toString() {
		return "QuizRoomBean [id=" + id + ", room_name=" + room_name + ", owner=" + owner + ", status=" + status
				+ ", member_count=" + member_count + ", members=" + members + "]";
	}
}
