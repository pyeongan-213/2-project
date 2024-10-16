package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "quiz_room")
public class QuizRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_room_id")
    private int quizRoomId;

    @Column(name = "quiz_room_name", nullable = false, length = 255)
    private String quizRoomName;

    @Column(name = "quiz_room_password", length = 255)
    private String quizRoomPassword;

    @Column(name = "owner", nullable = false, length = 255)
    private String owner;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "max_music", nullable = false)
    private int maxMusic;

    @Column(name = "quiz_room_type", length = 50, nullable = false)
    private String quizRoomType;

    @Version
    @Column(name = "version")
    private Integer version;
    
    // 기본 생성자
    public QuizRoom() {}

    
	public QuizRoom(int quizRoomId, String quizRoomName, String quizRoomPassword, String owner, int status,
			int memberCount, int maxCapacity, int maxMusic, String quizRoomType) {
		super();
		this.quizRoomId = quizRoomId;
		this.quizRoomName = quizRoomName;
		this.quizRoomPassword = quizRoomPassword;
		this.owner = owner;
		this.status = status;
		this.memberCount = memberCount;
		this.maxCapacity = maxCapacity;
		this.maxMusic = maxMusic;
		this.quizRoomType = quizRoomType;
	}

	public QuizRoom(String quizRoomName, String quizRoomPassword, String owner, int status, int memberCount,
			int maxCapacity, int maxMusic, String quizRoomType) {
		super();
		this.quizRoomName = quizRoomName;
		this.quizRoomPassword = quizRoomPassword;
		this.owner = owner;
		this.status = status;
		this.memberCount = memberCount;
		this.maxCapacity = maxCapacity;
		this.maxMusic = maxMusic;
		this.quizRoomType = quizRoomType;
	}


	public int getQuizRoomId() {
		return quizRoomId;
	}


	public void setQuizRoomId(int quizRoomId) {
		this.quizRoomId = quizRoomId;
	}


	public String getQuizRoomName() {
		return quizRoomName;
	}


	public void setQuizRoomName(String quizRoomName) {
		this.quizRoomName = quizRoomName;
	}


	public String getQuizRoomPassword() {
		return quizRoomPassword;
	}


	public void setQuizRoomPassword(String quizRoomPassword) {
		this.quizRoomPassword = quizRoomPassword;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getMemberCount() {
		return memberCount;
	}


	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}


	public int getMaxCapacity() {
		return maxCapacity;
	}


	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}


	public int getMaxMusic() {
		return maxMusic;
	}


	public void setMaxMusic(int maxMusic) {
		this.maxMusic = maxMusic;
	}


	public String getQuizRoomType() {
		return quizRoomType;
	}


	public void setQuizRoomType(String quizRoomType) {
		this.quizRoomType = quizRoomType;
	}

    
}
