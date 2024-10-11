package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "quiz_room") // 테이블 이름을 명시적으로 지정
public class QuizRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quiz_room_id") // 자동 증가 ID
	private int quizRoomId;

	@Column(name = "quiz_room_name", nullable = false, length = 255) // 퀴즈방 이름
	private String quizRoomName;

	@Column(name = "quiz_room_password", length = 255) // 퀴즈방 비밀번호
	private String quizRoomPassword;

	@Column(name = "owner", nullable = false, length = 255) // 방 소유자
	private String owner;

	@Column(name = "status", nullable = false) // 방 상태 (0: 닫힘, 1: 열림)
	private int status;

	@Column(name = "member_count", nullable = false) // 현재 방의 멤버 수
	private int memberCount;

	@Column(name = "max_capacity", nullable = false) // 최대인원 수
	private int maxCapacity;

	@Version // 낙관적 락을 위한 버전 필드
	@Column(name = "version")
	private Integer version;

	// 기본 생성자
	public QuizRoom() {
	}

	// 모든 필드를 포함하는 생성자
	public QuizRoom(String quizRoomName, String quizRoomPassword, String owner, int status, int memberCount,
			int maxCapacity) {
		this.quizRoomName = quizRoomName;
		this.quizRoomPassword = quizRoomPassword;
		this.owner = owner;
		this.status = status;
		this.memberCount = memberCount;
		this.maxCapacity = maxCapacity;
	}

	// Getter와 Setter
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

	public int getmaxCapacity() {
		return maxCapacity;
	}

	public void setmaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "QuizRoom [quizRoomId=" + quizRoomId + ", quizRoomName=" + quizRoomName + ", quizRoomPassword="
				+ quizRoomPassword + ", owner=" + owner + ", status=" + status + ", memberCount=" + memberCount
				+ ", maxcapacity=" + maxCapacity + ", version=" + version + "]";
	}

	
	
}
