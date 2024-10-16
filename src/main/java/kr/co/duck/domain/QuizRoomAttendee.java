package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// 기능: 퀴즈방과 유저를 연결하는 중간 Entity
@Entity
@Table(name = "quiz_room_attendee")
public class QuizRoomAttendee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quiz_room_member_id") // 칼럼명 지정
	private int quizRoomMemberId;

	@JoinColumn(name = "quiz_room_id") // 칼럼명 지정
	@OneToOne(fetch = FetchType.LAZY)
	private QuizRoom quizRoom;

	@JoinColumn(name = "member_id") // 칼럼명 지정
	@OneToOne(fetch = FetchType.LAZY)
	private Member member;

	@Column(name = "member_nickname", nullable = false) // 참가자들의 닉네임
	private String memberNickname;

	@Column(name = "join_members", nullable = false) // 현재 참가자
	private String joinmembers;

	// 기본 생성자
	public QuizRoomAttendee() {
	}

	// 모든 필드를 포함하는 생성자
	public QuizRoomAttendee(int quizRoomMemberId, QuizRoom quizRoom, Member member, String memberNickname,
			String joinmembers) {
		super();
		this.quizRoomMemberId = quizRoomMemberId;
		this.quizRoom = quizRoom;
		this.member = member;
		this.memberNickname = memberNickname;
		this.joinmembers = joinmembers;
	}

	// 게임룸과 멤버를 이용하는 생성자
	public QuizRoomAttendee(QuizRoom quizRoom, Member member) {
		this.quizRoom = quizRoom;
		this.member = member;
		this.memberNickname = member.getNickname();
	}

	public int getQuizRoomMemberId() {
		return quizRoomMemberId;
	}

	public void setQuizRoomMemberId(int quizRoomMemberId) {
		this.quizRoomMemberId = quizRoomMemberId;
	}

	public QuizRoom getQuizRoom() {
		return quizRoom;
	}

	public void setQuizRoom(QuizRoom quizRoom) {
		this.quizRoom = quizRoom;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getMemberNickname() {
		return memberNickname;
	}

	public void setMemberNickname(String memberNickname) {
		this.memberNickname = memberNickname;
	}

	public String getJoinmembers() {
		return joinmembers;
	}

	public void setJoinmembers(String joinmembers) {
		this.joinmembers = joinmembers;
	}

	@Override
	public String toString() {
		return "QuizRoomAttendee [quizRoomMemberId=" + quizRoomMemberId + ", quizRoom=" + quizRoom + ", member="
				+ member + ", memberNickname=" + memberNickname + ", joinmembers=" + joinmembers + "]";
	}

	
	
}
