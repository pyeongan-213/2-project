package kr.co.duck.domain;

import javax.persistence.*;

@Entity
public class QuizStartSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long quizSetId;

	@Column(nullable = false)
	private Long roomId;

	@Column(nullable = false)
	private String category;

	@Column
	private String keywordToMember; // JSON 형식의 문자열로 저장

	@Column(nullable = false)
	private Integer round;

	@Column(nullable = false)
	private Integer spotNum;

	@Column
	private String winner;

	@Column
	private Long quizStartTime;

	// 기본 생성자
	public QuizStartSet() {
	}

	// 매개변수 있는 생성자
	public QuizStartSet(Long quizSetId, Long roomId, String category, String keywordToMember, Integer round,
			Integer spotNum, String winner, Long quizStartTime) {
		this.quizSetId = quizSetId;
		this.roomId = roomId;
		this.category = category;
		this.keywordToMember = keywordToMember;
		this.round = round;
		this.spotNum = spotNum;
		this.winner = winner;
		this.quizStartTime = quizStartTime;
	}

	// Getter 메서드
	public Long getQuizSetId() {
		return quizSetId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public String getCategory() {
		return category;
	}

	public String getKeywordToMember() {
		return keywordToMember;
	}

	public Integer getRound() {
		return round;
	}

	public Integer getSpotNum() {
		return spotNum;
	}

	public String getWinner() {
		return winner;
	}

	public Long getQuizStartTime() {
		return quizStartTime;
	}

	// Setter 메서드
	public void setSpotNum(Integer spotNum) {
		this.spotNum = spotNum;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public void setKeywordToMember(String keywordToMember) {
		this.keywordToMember = keywordToMember;
	}

	public void setQuizStartTime(Long quizStartTime) {
		this.quizStartTime = quizStartTime;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "QuizStartSet [quizSetId=" + quizSetId + ", roomId=" + roomId + ", category=" + category
				+ ", keywordToMember=" + keywordToMember + ", round=" + round + ", spotNum=" + spotNum + ", winner="
				+ winner + ", quizStartTime=" + quizStartTime + "]";
	}
}
