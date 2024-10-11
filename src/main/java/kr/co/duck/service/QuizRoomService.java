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
	private final ChatService chatService;

	// 생성자: 의존성 주입을 통해 필요한 서비스와 리포지토리 초기화
	public QuizRoomService(MemberCommand memberCommand, QuizQuery quizQuery, QuizCommand quizCommand,
			SessionRepository sessionRepository, QuizRoomRepository quizRoomRepository, ChatService chatService) {
		this.memberCommand = memberCommand;
		this.quizQuery = quizQuery;
		this.quizCommand = quizCommand;
		this.sessionRepository = sessionRepository;
		this.quizRoomRepository = quizRoomRepository;
		this.chatService = chatService;
	}

	/**
	 * 사용자의 게임 통계를 보장하기 위해 Member 엔티티에서 MemberGameStats가 null인 경우
	 * 새로 생성하여 설정하는 메서드.
	 *
	 * @param member 현재 게임 참여 회원
	 */
	private void ensureMemberGameStats(Member member) {
		if (member.getMemberGameStats() == null) {
			member.setMemberGameStats(new MemberGameStats(member));
		}
	}

	
	 //퀴즈 방 ID로 퀴즈 방을 조회하는 메서드.
	 //해당 방의 참가자 목록도 조회하고, QuizRoomBean 객체로 반환.
	@Transactional(readOnly = true)
	public QuizRoomBean findRoomById(int roomId) {
	    // 퀴즈방을 ID로 조회
	    QuizRoom quizRoom = quizQuery.findQuizRoomByRoomId(roomId);
	    if (quizRoom == null) {
	        throw new CustomException(NOT_EXIST_ROOMS);
	    }

	    // 해당 퀴즈방에 참여한 멤버 리스트 조회
	    List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(quizRoom);
	    int memberCount = quizRoomAttendeeList.size(); // 멤버 수 계산

	    // QuizRoomBean 객체 반환 (QuizRoomAttendee에서 멤버 정보 관리)
	    return new QuizRoomBean(
	        quizRoom.getQuizRoomId(),
	        quizRoom.getQuizRoomName(),
	        quizRoom.getQuizRoomPassword(),
	        quizRoom.getOwner(),
	        quizRoom.getStatus(),
	        memberCount,
	        quizRoom.getmaxCapacity()
	    );
	}


	/**
	 * 퀴즈 방 목록을 페이지네이션하여 조회하는 메서드.
	 *
	 * @param pageable 페이지네이션 정보를 담은 객체
	 * @return QuizRoomListBean 퀴즈 방 목록과 전체 페이지 수를 담은 객체
	 */
	@Transactional
	public QuizRoomListBean getAllQuizRooms(Pageable pageable) {
	    // 페이지 네이션을 통해 퀴즈방 목록을 가져옴
	    Page<QuizRoom> rooms = quizQuery.findQuizRoomByPageable(pageable);
	    List<QuizRoomBean> quizRoomList = new ArrayList<>();

	    // 각 퀴즈방에 대해 처리
	    for (QuizRoom room : rooms) {
	        // 해당 퀴즈방에 참여한 참가자 목록을 가져옴
	        List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(room);
	        int memberCount = quizRoomAttendeeList.size(); // 멤버 수 계산

	        // QuizRoomBean 객체 생성
	        QuizRoomBean quizRoomBean = new QuizRoomBean(
	            room.getQuizRoomId(),
	            room.getQuizRoomName(),
	            room.getQuizRoomPassword(),
	            room.getOwner(),
	            room.getStatus(),
	            memberCount, // 멤버 수
	            room.getmaxCapacity() // 최대 수용 인원
	        );

	        // 생성한 퀴즈방 빈을 리스트에 추가
	        quizRoomList.add(quizRoomBean);
	    }

	    // 전체 페이지 수 계산
	    int totalPage = rooms.getTotalPages();

	    // QuizRoomListBean 반환
	    return new QuizRoomListBean(totalPage, quizRoomList);
	}



	 //키워드를 기반으로 퀴즈 방을 검색하는 메서드.
	 //@return QuizRoomListBean 검색된 퀴즈 방 목록과 전체 페이지 수를 담은 객체
	public QuizRoomListBean searchQuizRoom(Pageable pageable, String keyword) {
	    // 키워드를 통해 퀴즈방을 검색
	    Page<QuizRoom> rooms = quizRoomRepository.findByQuizRoomNameContaining(pageable, keyword);

	    // 검색 결과가 없으면 예외 발생
	    if (rooms.isEmpty()) {
	        throw new CustomException(NOT_EXIST_ROOMS);
	    }

	    List<QuizRoomBean> quizRoomList = new ArrayList<>();

	    // 검색된 각 퀴즈방에 대해 처리
	    for (QuizRoom room : rooms) {
	        // 해당 퀴즈방에 참가한 참가자 목록을 가져옴
	        List<QuizRoomAttendee> quizRoomAttendeeList = quizQuery.findAttendeeByQuizRoom(room);
	        int memberCount = quizRoomAttendeeList.size(); // 멤버 수 계산

	        // QuizRoomBean 객체 생성
	        QuizRoomBean quizRoomBean = new QuizRoomBean(
	            room.getQuizRoomId(),
	            room.getQuizRoomName(),
	            room.getQuizRoomPassword(),
	            room.getOwner(),
	            room.getStatus(),
	            memberCount, // 멤버 수
	            room.getmaxCapacity() // 최대 수용 인원
	        );

	        // 생성된 퀴즈방 빈을 리스트에 추가
	        quizRoomList.add(quizRoomBean);
	    }

	    // 전체 페이지 수 계산
	    int totalPage = rooms.getTotalPages();

	    // QuizRoomListBean 반환
	    return new QuizRoomListBean(totalPage, quizRoomList);
	}


	/**
	 * 퀴즈 방을 생성하는 메서드.
	 * 새로운 방을 만들고, 방장 정보를 포함하여 QuizRoomBean 객체로 반환.
	 *
	 * @param quizRoomBean 퀴즈 방 정보
	 * @param memberBean 퀴즈 방 생성자 정보
	 * @return QuizRoomBean 생성된 퀴즈 방 정보를 담은 객체
	 */
	@Transactional
	public QuizRoomBean createRoom(QuizRoomBean quizRoomBean, MemberBean memberBean) {
	    // 멤버 조회
	    Member member = memberCommand.findMemberById(memberBean.getMember_id());
	    ensureMemberGameStats(member);

	    // 게임 통계 업데이트
	    MemberGameStats stats = member.getMemberGameStats();
	    stats.setMakeRoomNum(stats.getMakeRoomNum() + 1);

	    // 멤버 저장
	    memberCommand.saveMember(member);

	    // 퀴즈 방 생성
	    QuizRoom quizRoom = new QuizRoom(quizRoomBean.getQuizRoomName(), quizRoomBean.getQuizRoomPassword(),
	            member.getNickname(), 1, quizRoomBean.getMemberCount(), quizRoomBean.getMaxcapacity());

	    // 퀴즈 방 저장
	    quizCommand.saveQuizRoom(quizRoom);

	    // 참여자 추가
	    QuizRoomAttendee joinMember = new QuizRoomAttendee(quizRoom, member); // 변수명을 joinMember로 변경
	    quizCommand.saveQuizRoomAttendee(joinMember);

	    // 생성된 퀴즈방 정보를 반환
	    return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
	            quizRoom.getOwner(), quizRoom.getStatus(), quizRoom.getMemberCount(), quizRoom.getmaxCapacity());
	}


	/**
	 * 퀴즈 방에 입장하는 메서드.
	 * 비밀번호 검증 후 방에 입장하며, 참가자 목록에 추가.
	 *
	 * @param roomId 퀴즈 방 ID
	 * @param memberBean 입장하는 회원 정보
	 * @param roomPassword 퀴즈 방 비밀번호 (필요한 경우)
	 * @return Map<String, String> 입장한 방의 정보
	 */
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

	/**
	 * 퀴즈 방에서 나가는 메서드.
	 * 방 소유자가 나가면 새로운 소유자를 설정하고, 마지막 참가자가 나가면 방을 삭제.
	 *
	 * @param roomId 퀴즈 방 ID
	 * @param memberBean 나가는 회원 정보
	 */
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

		chatService.sendQuizMessage(roomId, QuizMessage.MessageType.LEAVE, contentSet, member.getNickname());

		// 방 소유자가 방을 나갔고, 남아있는 참여자가 있으면 새 소유자를 할당
		if (member.getNickname().equals(enterQuizRoom.getOwner()) && !existingAttendees.isEmpty()) {
			String nextOwner = existingAttendees.get((int) (Math.random() * existingAttendees.size()))
					.getMemberNickname();
			enterQuizRoom.setOwner(nextOwner);
			chatService.sendQuizMessage(roomId, QuizMessage.MessageType.NEWOWNER, null, nextOwner);
		}
	}
}
