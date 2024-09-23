package kr.co.duck.beans;

public class CommentBean {

	private int comment_id;
	private int board_id2;
	private int member_id;
	private String comment_text;
	private String date;

	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getBoard_id2() {
		return board_id2;
	}
	public void setBoard_id2(int board_id2) {
		this.board_id2 = board_id2;
	}
	public int getMember_id() {
		return member_id;
	}
	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}
	public String getComment_text() {
		return comment_text;
	}
	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
