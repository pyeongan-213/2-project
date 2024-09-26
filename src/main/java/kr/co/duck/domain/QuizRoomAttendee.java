package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

// 기능: 퀴즈방과 유저를 연결하는 중간 Entity
@Entity
public class QuizRoomAttendee extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quizRoomMemberId; // 변경: Long -> int

	@JoinColumn(name = "quizroomid")
	@OneToOne(fetch = FetchType.LAZY)
	private QuizRoom quizRoom;

	@JoinColumn(name = "memberid")
	@OneToOne(fetch = FetchType.LAZY)
	private Member member;

	@Column(nullable = false)
	private String memberNickname;

	// 기본 생성자
	public QuizRoomAttendee() {
	}

	// 모든 필드를 포함하는 생성자
	public QuizRoomAttendee(int quizRoomMemberId, QuizRoom quizRoom, Member member, String memberNickname) {
																												
																												
		this.quizRoomMemberId = quizRoomMemberId;
		this.quizRoom = quizRoom;
		this.member = member;
		this.memberNickname = memberNickname;
	}

	// 게임룸과 멤버를 이용하는 생성자
	public QuizRoomAttendee(QuizRoom quizRoom, Member member) {
		this.quizRoom = quizRoom;
		this.member = member;
		this.memberNickname = member.getNickname();
	}

	// Getter 메서드
	public int getQuizRoomMemberId() { 
		return quizRoomMemberId;
	}

	public QuizRoom getQuizRoom() {
		return quizRoom;
	}

	public Member getMember() {
		return member;
	}

	public String getMemberNickname() {
		return memberNickname;
	}

	@Override
	public String toString() {
		return "QuizRoomAttendee{" + "quizRoomMemberId=" + quizRoomMemberId + ", quizRoom=" + quizRoom + ", member="
				+ member + ", memberNickname='" + memberNickname + '\'' + '}';
	}
}
