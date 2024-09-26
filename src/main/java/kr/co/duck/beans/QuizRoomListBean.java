package kr.co.duck.beans;

import java.util.List;

public class QuizRoomListBean {
	private int total_page;
	private List<QuizRoomBean> quiz_room_bean_list;

	// 기본 생성자
	public QuizRoomListBean() {
	}

	// 모든 필드를 포함하는 생성자
	public QuizRoomListBean(int total_page, List<QuizRoomBean> quiz_room_bean_list) {
		this.total_page = total_page;
		this.quiz_room_bean_list = quiz_room_bean_list;
	}

	// Getter와 Setter
	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public List<QuizRoomBean> getQuiz_room_bean_list() {
		return quiz_room_bean_list;
	}

	public void setQuiz_room_bean_list(List<QuizRoomBean> quiz_room_bean_list) {
		this.quiz_room_bean_list = quiz_room_bean_list;
	}

	@Override
	public String toString() {
		return "QuizRoomListBean [total_page=" + total_page + ", quiz_room_bean_list=" + quiz_room_bean_list + "]";
	}
}
