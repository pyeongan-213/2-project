package kr.co.duck.service;

import static kr.co.duck.util.StatusCode.NOT_EXIST_ROOMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.beans.QuizRoomBean;
import kr.co.duck.beans.QuizRoomListBean;
import kr.co.duck.domain.Member;
import kr.co.duck.domain.MemberCommand;
import kr.co.duck.domain.MemberGameStats;
import kr.co.duck.domain.QuizCommand;
import kr.co.duck.domain.QuizMessage;
import kr.co.duck.domain.QuizQuery;
import kr.co.duck.domain.QuizRoom;
import kr.co.duck.domain.QuizRoomAttendee;
import kr.co.duck.repository.QuizRoomRepository;
import kr.co.duck.repository.SessionRepository;
import kr.co.duck.util.CustomException;
import kr.co.duck.util.StatusCode;

@Service
public class QuizRoomService {
	private final MemberCommand memberCommand;
	private final QuizQuery quizQuery;
	private final QuizCommand quizCommand;
	private final SessionRepository sessionRepository;
	private final QuizRoomRepository quizRoomRepository;
	private final ChatService chatService; // ChatService 의존성 추가

	public QuizRoomService(MemberCommand memberCommand, QuizQuery quizQuery,
			QuizCommand quizCommand, SessionRepository sessionRepository, QuizRoomRepository quizRoomRepository, 
			ChatService chatService) { // ChatService 추가
		this.memberCommand = memberCommand;
		this.quizQuery = quizQuery;
		this.quizCommand = quizCommand;
		this.sessionRepository = sessionRepository;
		this.quizRoomRepository = quizRoomRepository;
		this.chatService = chatService; // ChatService 초기화
	}

	// MemberGameStats가 null인 경우 생성하여 설정하는 메서드
	private void ensureMemberGameStats(Member member) {
		if (member.getMemberGameStats() == null) {
			member.setMemberGameStats(new MemberGameStats(member));
		}
	}

	// 퀴즈방 ID로 퀴즈방 조회
	@Transactional(readOnly = true)
	public QuizRoomBean findRoomById(int roomId) {
		QuizRoom quizRoom = quizQuery.findQuizRoomByRoomId(roomId);
		if (quizRoom == null) {
			throw new CustomException(NOT_EXIST_ROOMS);
		}

		List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(quizRoom);
		List<MemberBean> memberList = new ArrayList<>();
		for (QuizRoomAttendee quizRoomAttendee : quizRoomAttendeeList) {
			Member eachMember = memberCommand.findMemberById(quizRoomAttendee.getMember().getMemberId());
			MemberBean memberBean = new MemberBean(eachMember.getMemberId(), eachMember.getNickname(),
					eachMember.getEmail());
			memberList.add(memberBean);
		}

		String membersString = memberList.stream().map(member -> String.valueOf(member.getMember_id()))
				.reduce((a, b) -> a + "," + b).orElse("");

		return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
				quizRoom.getOwner(), quizRoom.getStatus(), memberList.size(), membersString);
	}

	// 퀴즈방 목록 조회
	@Transactional
	public QuizRoomListBean getAllQuizRooms(Pageable pageable) {
		Page<QuizRoom> rooms = quizQuery.findQuizRoomByPageable(pageable);
		List<QuizRoomBean> quizRoomList = new ArrayList<>();

		for (QuizRoom room : rooms) {
			List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(room);
			List<MemberBean> memberList = new ArrayList<>();

			for (QuizRoomAttendee quizRoomAttendee : quizRoomAttendeeList) {
				Member eachMember = memberCommand.findMemberById(quizRoomAttendee.getMember().getMemberId());
				MemberBean memberBean = new MemberBean(eachMember.getMemberId(), eachMember.getNickname(),
						eachMember.getEmail());
				memberList.add(memberBean);
			}

			String membersString = memberList.stream().map(member -> String.valueOf(member.getMember_id()))
					.reduce((a, b) -> a + "," + b).orElse("");

			QuizRoomBean quizRoomBean = new QuizRoomBean(room.getQuizRoomId(), room.getQuizRoomName(),
					room.getQuizRoomPassword(), room.getOwner(), room.getStatus(), memberList.size(), membersString);
			quizRoomList.add(quizRoomBean);
		}

		int totalPage = rooms.getTotalPages();
		return new QuizRoomListBean(totalPage, quizRoomList);
	}

	// 퀴즈방 검색
	public QuizRoomListBean searchQuizRoom(Pageable pageable, String keyword) {
		Page<QuizRoom> rooms = quizRoomRepository.findByQuizRoomNameContaining(pageable, keyword);

		if (rooms.isEmpty()) {
			throw new CustomException(NOT_EXIST_ROOMS);
		}

		List<QuizRoomBean> quizRoomList = new ArrayList<>();
		for (QuizRoom room : rooms) {
			List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(room);
			List<MemberBean> memberList = new ArrayList<>();

			for (QuizRoomAttendee quizRoomAttendee : quizRoomAttendeeList) {
				Member eachMember = memberCommand.findMemberById(quizRoomAttendee.getMember().getMemberId());
				MemberBean memberBean = new MemberBean(eachMember.getMemberId(), eachMember.getNickname(),
						eachMember.getEmail());
				memberList.add(memberBean);
			}

			String membersString = memberList.stream().map(member -> String.valueOf(member.getMember_id()))
					.reduce((a, b) -> a + "," + b).orElse("");

			QuizRoomBean quizRoomBean = new QuizRoomBean(room.getQuizRoomId(), room.getQuizRoomName(),
					room.getQuizRoomPassword(), room.getOwner(), room.getStatus(), memberList.size(), membersString);
			quizRoomList.add(quizRoomBean);
		}

		int totalPage = rooms.getTotalPages();
		return new QuizRoomListBean(totalPage, quizRoomList);
	}

	// 퀴즈방 생성
	@Transactional
	public QuizRoomBean createRoom(QuizRoomBean quizRoomBean, MemberBean memberBean) {
		Member member = memberCommand.findMemberById(memberBean.getMember_id());
		ensureMemberGameStats(member);

		// 게임 통계 업데이트
		MemberGameStats stats = member.getMemberGameStats();
		stats.setMakeRoomNum(stats.getMakeRoomNum() + 1);

		memberCommand.saveMember(member);

		QuizRoom quizRoom = new QuizRoom(quizRoomBean.getQuizRoomName(), quizRoomBean.getQuizRoomPassword(),
				member.getNickname(), 1, quizRoomBean.getMemberCount(), quizRoomBean.getMembers());

		quizCommand.saveQuizRoom(quizRoom);

		QuizRoomAttendee quizRoomAttendee = new QuizRoomAttendee(quizRoom, member);
		quizCommand.saveQuizRoomAttendee(quizRoomAttendee);

		return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
				quizRoom.getOwner(), quizRoom.getStatus(), quizRoom.getMemberCount(), quizRoomBean.getMembers());
	}

	// 퀴즈방 입장
	@Transactional
	public Map<String, String> enterQuizRoom(int roomId, MemberBean memberBean, String roomPassword) {
		Member member = memberCommand.findMemberById(memberBean.getMember_id());
		QuizRoom enterQuizRoom = quizQuery.findQuizRoomByRoomIdLock(roomId);

		// 중복된 사용자 방 참여 방지
		List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(enterQuizRoom);
		boolean isAlreadyParticipant = quizRoomAttendeeList.stream()
				.anyMatch(attendee -> attendee.getMember().getMemberId() == member.getMemberId());
		if (isAlreadyParticipant) {
			throw new CustomException(StatusCode.MEMBER_DUPLICATED, "이미 방에 참여 중입니다.");
		}

		// 비밀번호가 필요한 방일 경우에만 비밀번호 확인
		if (enterQuizRoom.getQuizRoomPassword() != null && !enterQuizRoom.getQuizRoomPassword().isEmpty()) {
			if (!enterQuizRoom.getQuizRoomPassword().equals(roomPassword)) {
				throw new CustomException(StatusCode.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
			}
		}

		ensureMemberGameStats(member);

		MemberGameStats stats = member.getMemberGameStats();
		stats.setEnterGameNum(stats.getEnterGameNum() + 1);
		memberCommand.saveMember(member);

		QuizRoomAttendee newAttendee = new QuizRoomAttendee(enterQuizRoom, member);
		quizCommand.saveQuizRoomAttendee(newAttendee);

		Map<String, String> roomInfo = new HashMap<>();
		roomInfo.put("quizRoomName", enterQuizRoom.getQuizRoomName());
		roomInfo.put("roomId", String.valueOf(enterQuizRoom.getQuizRoomId()));
		roomInfo.put("owner", enterQuizRoom.getOwner());
		roomInfo.put("status", String.valueOf(enterQuizRoom.getStatus()));

		return roomInfo;
	}


	// 방 나가기
	@Transactional
	public void roomExit(int roomId, MemberBean memberBean) {
		Member member = memberCommand.findMemberById(memberBean.getMember_id());
		QuizRoom enterQuizRoom = quizQuery.findQuizRoomByRoomId(roomId);

		// 참여자가 방에 있는지 확인하고, 참여자를 제거
		QuizRoomAttendee quizRoomAttendee = quizQuery.findAttendeeByMemberAndRoom(member, enterQuizRoom);
		if (quizRoomAttendee != null) {
			quizCommand.deleteQuizRoomAttendee(quizRoomAttendee);
		} else {
			throw new CustomException(StatusCode.BAD_REQUEST, "방에 참여 중인 사용자가 아닙니다.");
		}

		// 방의 현재 참여자 목록을 업데이트
		List<QuizRoomAttendee> existingAttendees = quizQuery.findAttendeeByQuizRoom(enterQuizRoom);

		// 방에 남은 참여자가 없으면 방을 삭제
		if (existingAttendees.isEmpty()) {
			quizCommand.deleteQuizRoom(enterQuizRoom);
			sessionRepository.deleteAllClientsInRoom(roomId);
			return;
		}

		// 방에서 나가는 사용자에 대한 메시지 전송
		Map<String, Object> contentSet = new HashMap<>();
		contentSet.put("memberCount", existingAttendees.size());
		contentSet.put("alert", member.getNickname() + " 님이 방을 나가셨습니다!");

		chatService.sendQuizMessage(roomId, QuizMessage.MessageType.LEAVE, contentSet, member.getNickname()); // ChatService 호출

		// 방 소유자가 방을 나갔고, 남아있는 참여자가 있으면 새 소유자를 할당
		if (member.getNickname().equals(enterQuizRoom.getOwner()) && !existingAttendees.isEmpty()) {
			String nextOwner = existingAttendees.get((int) (Math.random() * existingAttendees.size()))
					.getMemberNickname();
			enterQuizRoom.setOwner(nextOwner);
			chatService.sendQuizMessage(roomId, QuizMessage.MessageType.NEWOWNER, null, nextOwner); // ChatService 호출
		}
	}
}