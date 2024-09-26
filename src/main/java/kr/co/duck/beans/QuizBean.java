package kr.co.duck.beans;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quiz")
public class QuizBean {

	@Id
	@Column(name = "quiz_id")
	private int quizId; // 퀴즈 식별 아이디

	@Column(name = "quiz_title")
	private String quizTitle; // 퀴즈 제목

	@Column(name = "quiz_text")
	private String quizText; // 퀴즈 설명

	@Column(name = "quiz_question_type")
	private String quizQuestionType; // 퀴즈 유형 (예: 노래 가사 맞추기)

	@Column(name = "quiz_answer")
	private String quizAnswer; // 퀴즈 정답

	// Assuming songIds is a list of SongBeans and not directly stored in the quiz
	// table
	private List<SongBean> songIds; // 퀴즈에 사용할 노래 정보 식별자 목록

	private List<Integer> quizJoinIds; // 퀴즈에 참가한 사람들의 식별자 목록

	@Column(name = "quiz_score")
	private int quizScore; // 퀴즈 정답 점수

	@Column(name = "quiz_hint")
	private String quizHint; // 퀴즈 힌트

	@Column(name = "join_user")
	private int joinUser; // 첫 번째로 입장한 사람 (방장) 식별자

	@Column(name = "user_id")
	private int userId; // 유저 식별번호

	// Getter 및 Setter
	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	public String getQuizText() {
		return quizText;
	}

	public void setQuizText(String quizText) {
		this.quizText = quizText;
	}

	public String getQuizQuestionType() {
		return quizQuestionType;
	}

	public void setQuizQuestionType(String quizQuestionType) {
		this.quizQuestionType = quizQuestionType;
	}

	public String getQuizAnswer() {
		return quizAnswer;
	}

	public void setQuizAnswer(String quizAnswer) {
		this.quizAnswer = quizAnswer;
	}

	public List<SongBean> getSongIds() {
		return songIds;
	}

	public void setSongIds(List<SongBean> songIds) {
		this.songIds = songIds;
	}

	public List<Integer> getQuizJoinIds() {
		return quizJoinIds;
	}

	public void setQuizJoinIds(List<Integer> quizJoinIds) {
		this.quizJoinIds = quizJoinIds;
	}

	public int getQuizScore() {
		return quizScore;
	}

	public void setQuizScore(int quizScore) {
		this.quizScore = quizScore;
	}

	public String getQuizHint() {
		return quizHint;
	}

	public void setQuizHint(String quizHint) {
		this.quizHint = quizHint;
	}

	public int getJoinUser() {
		return joinUser;
	}

	public void setJoinUser(int joinUser) {
		this.joinUser = joinUser;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "QuizBean [quizId=" + quizId + ", quizTitle=" + quizTitle + ", quizText=" + quizText
				+ ", quizQuestionType=" + quizQuestionType + ", quizAnswer=" + quizAnswer + ", quizScore=" + quizScore
				+ ", quizHint=" + quizHint + ", joinUser=" + joinUser + ", userId=" + userId + "]";
	}
}
