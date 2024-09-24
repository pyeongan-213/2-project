package kr.co.duck.beans;

public class RoomBean {
	private int roomId;
	private String roomName;
	private int maxUsers;
	private int currentUsers;

	// Getters and Setters
	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	public int getCurrentUsers() {
		return currentUsers;
	}

	public void setCurrentUsers(int currentUsers) {
		this.currentUsers = currentUsers;
	}
}
