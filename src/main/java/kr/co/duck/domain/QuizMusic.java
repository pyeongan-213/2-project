package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quiz_music")
public class QuizMusic {

	@Id
	@Column(name = "quiz_id")
	private int quizId; // 퀴즈 ID

	@Id
	@Column(name = "music_id")
	private int musicId; // 음악 ID

	@Column(name = "music_name", length = 255)
	private String musicName; // 노래 제목

	@Column(name = "video_code", length = 255)
	private String videoCode; // YouTube 비디오 코드

	@Column(name = "start_time")
	private Integer startTime; // 음악의 시작 시간 (초 단위)

	@Column(name = "tags", length = 1000)
	private String tags; // 태그 (콤마로 구분된 문자열)

	@Column(name = "answer", length = 1000)
	private String answer; // 정답 (콤마로 구분된 문자열)

	public QuizMusic() {

	}
	
	public QuizMusic(int quizId, int musicId, String musicName, String videoCode, Integer startTime, String tags,
			String answer) {
		super();
		this.quizId = quizId;
		this.musicId = musicId;
		this.musicName = musicName;
		this.videoCode = videoCode;
		this.startTime = startTime;
		this.tags = tags;
		this.answer = answer;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getVideoCode() {
		return videoCode;
	}

	public void setVideoCode(String videoCode) {
		this.videoCode = videoCode;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
