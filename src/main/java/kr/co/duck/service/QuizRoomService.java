package kr.co.duck.service;

import static kr.co.duck.util.StatusCode.NOT_EXIST_ROOMS;

import java.util.ArrayList;
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
import kr.co.duck.domain.QuizQuery;
import kr.co.duck.domain.QuizRoom;
import kr.co.duck.domain.QuizRoomAttendee;
import kr.co.duck.repository.QuizRoomAttendeeRepository;
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
	private final ChatService chatService;
	private final QuizRoomAttendeeRepository quizRoomAttendeeRepository;

	public QuizRoomService(MemberCommand memberCommand, QuizQuery quizQuery, QuizCommand quizCommand,
			SessionRepository sessionRepository, QuizRoomRepository quizRoomRepository, ChatService chatService,
			QuizRoomAttendeeRepository quizRoomAttendeeRepository) {
		this.memberCommand = memberCommand;
		this.quizQuery = quizQuery;
		this.quizCommand = quizCommand;
		this.sessionRepository = sessionRepository;
		this.quizRoomRepository = quizRoomRepository;
		this.chatService = chatService;
		this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
	}

	// 게임 통계 보장 메서드
	private void ensureMemberGameStats(Member member) {
		if (member.getMemberGameStats() == null) {
			member.setMemberGameStats(new MemberGameStats(member));
		}
	}

	// **퀴즈 방 목록을 페이지네이션하여 조회하는 메서드**
	@Transactional(readOnly = true)
	public QuizRoomListBean getAllQuizRooms(Pageable pageable) {
		Page<QuizRoom> rooms = quizQuery.findQuizRoomByPageable(pageable);

		if (rooms.isEmpty()) {
			throw new CustomException(StatusCode.NOT_EXIST_ROOMS, "퀴즈 방이 존재하지 않습니다.");
		}

		List<QuizRoomBean> quizRoomList = new ArrayList<>();
		rooms.forEach(room -> {
			String quizQuestionType = room.getQuiz() != null ? room.getQuiz().getQuizQuestionType() : "노래 제목 맞추기";
			quizRoomList.add(new QuizRoomBean(room.getQuizRoomId(), room.getQuizRoomName(), room.getQuizRoomPassword(),
					room.getOwner(), room.getStatus(), room.getMemberCount(), room.getMaxCapacity(), room.getMaxMusic(),
					quizQuestionType));
		});

		int totalPages = rooms.getTotalPages();
		return new QuizRoomListBean(totalPages, quizRoomList);
	}

	// 방 ID로 퀴즈 방 조회
	@Transactional(readOnly = true)
	public QuizRoomBean findRoomById(int roomId) {
		QuizRoom quizRoom = quizQuery.findQuizRoomByRoomId(roomId);
		if (quizRoom == null) {
			throw new CustomException(NOT_EXIST_ROOMS);
		}

		List<QuizRoomAttendee> attendees = quizRoomAttendeeRepository.findByQuizRoom(quizRoom);
		int memberCount = attendees.size();

		return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
				quizRoom.getOwner(), quizRoom.getStatus(), memberCount, quizRoom.getMaxCapacity(),
				quizRoom.getMaxMusic(),
				quizRoom.getQuiz() != null ? quizRoom.getQuiz().getQuizQuestionType() : "기본 퀴즈 유형");
	}

	// 방 검색
	@Transactional
	public QuizRoomListBean searchQuizRoom(Pageable pageable, String keyword) {
		var rooms = quizRoomRepository.findByQuizRoomNameContaining(pageable, keyword);
		if (rooms.isEmpty()) {
			throw new CustomException(NOT_EXIST_ROOMS);
		}

		List<QuizRoomBean> quizRoomList = new ArrayList<>();
		rooms.forEach(room -> {
			quizRoomList.add(new QuizRoomBean(room.getQuizRoomId(), room.getQuizRoomName(), room.getQuizRoomPassword(),
					room.getOwner(), room.getStatus(), room.getMemberCount(), room.getMaxCapacity(), room.getMaxMusic(),
					room.getQuiz() != null ? room.getQuiz().getQuizQuestionType() : "노래 제목 맞추기"));
		});

		return new QuizRoomListBean(rooms.getTotalPages(), quizRoomList);
	}

	// 참가자들의 닉네임 목록 가져오기
	@Transactional(readOnly = true)
	public List<String> getAttendeesNicknamesByRoomId(int roomId) {
		List<QuizRoomAttendee> attendees = quizRoomAttendeeRepository.findByQuizRoomQuizRoomId(roomId);

		if (attendees.isEmpty()) {
			throw new CustomException(StatusCode.NOT_FOUND_MEMBER, "참가자가 존재하지 않습니다.");
		}

		List<String> nicknames = new ArrayList<>();
		for (QuizRoomAttendee attendee : attendees) {
			nicknames.add(attendee.getMember().getNickname());
		}
		return nicknames;
	}

	// 방 생성
	@Transactional
	public QuizRoomBean createRoom(QuizRoomBean quizRoomBean, MemberBean memberBean) {
		Member member = memberCommand.findMemberById(memberBean.getMember_id());
		ensureMemberGameStats(member);

		member.getMemberGameStats().setMakeRoomNum(member.getMemberGameStats().getMakeRoomNum() + 1);
		memberCommand.saveMember(member);

		QuizRoom quizRoom = new QuizRoom(quizRoomBean.getQuizRoomName(), quizRoomBean.getQuizRoomPassword(),
				member.getNickname(), 1, 0, quizRoomBean.getMaxCapacity(), quizRoomBean.getMaxMusic());
		quizCommand.saveQuizRoom(quizRoom);

		quizCommand.saveQuizRoomAttendee(new QuizRoomAttendee(quizRoom, member));
		return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
				quizRoom.getOwner(), quizRoom.getStatus(), quizRoom.getMemberCount(), quizRoom.getMaxCapacity(),
				quizRoom.getMaxMusic(), quizRoomBean.getQuizQuestionType());
	}

	// 방 입장
	@Transactional
	public Map<String, String> enterQuizRoom(int roomId, MemberBean memberBean, String roomPassword) {
		Member member = memberCommand.findMemberById(memberBean.getMember_id());
		QuizRoom quizRoom = quizQuery.findQuizRoomByRoomIdLock(roomId);

		List<QuizRoomAttendee> attendees = quizQuery.findAttendeeByQuizRoom(quizRoom);
		if (attendees.stream().anyMatch(att -> att.getMember().getMemberId() == member.getMemberId())) {
			throw new CustomException(StatusCode.MEMBER_DUPLICATED, "이미 방에 참여 중입니다.");
		}

		if (!quizRoom.getQuizRoomPassword().isEmpty() && !quizRoom.getQuizRoomPassword().equals(roomPassword)) {
			throw new CustomException(StatusCode.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
		}

		ensureMemberGameStats(member);
		member.getMemberGameStats().setEnterGameNum(member.getMemberGameStats().getEnterGameNum() + 1);
		memberCommand.saveMember(member);

		quizCommand.saveQuizRoomAttendee(new QuizRoomAttendee(quizRoom, member));
		quizRoom.setMemberCount(quizRoom.getMemberCount() + 1);
		quizCommand.saveQuizRoom(quizRoom);

		return Map.of("quizRoomName", quizRoom.getQuizRoomName(), "roomId", String.valueOf(quizRoom.getQuizRoomId()),
				"owner", quizRoom.getOwner(), "status", String.valueOf(quizRoom.getStatus()));
	}

	// 일반 채팅 메시지 처리
	public void processChatMessage(int roomId, String sender, String content) {
		chatService.sendChatMessage(roomId, sender, content);
	}

	// 방 나가기
	@Transactional
	public void roomExit(int roomId, MemberBean memberBean) {
		Member member = memberCommand.findMemberById(memberBean.getMember_id());
		QuizRoom quizRoom = quizQuery.findQuizRoomByRoomId(roomId);

		QuizRoomAttendee attendee = quizQuery.findAttendeeByMemberAndRoom(member, quizRoom);
		if (attendee == null) {
			throw new CustomException(StatusCode.BAD_REQUEST, "방에 참여 중인 사용자가 아닙니다.");
		}
		quizCommand.deleteQuizRoomAttendee(attendee);

		quizRoom.setMemberCount(quizRoom.getMemberCount() - 1);
		quizCommand.saveQuizRoom(quizRoom);

		chatService.sendChatMessage(roomId, "알림", member.getNickname() + " 님이 나갔습니다!");

		if (quizRoom.getMemberCount() == 0) {
			quizCommand.deleteQuizRoom(quizRoom);
			sessionRepository.deleteAllClientsInRoom(roomId);
		}
	}
}
