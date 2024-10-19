package kr.co.duck.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizQuery;
import kr.co.duck.domain.QuizRoom;
import kr.co.duck.domain.QuizRoomAttendee;
import kr.co.duck.repository.MemberGameStatsRepository;  // 추가
import kr.co.duck.repository.MemberRepository;
import kr.co.duck.repository.QuizMusicRepository;
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
	private final QuizMusicRepository quizMusicRepository;
	private final MemberGameStatsRepository memberGameStatsRepository; // 추가
	private static final Logger log = LoggerFactory.getLogger(QuizRoomService.class);

	@Autowired
	private MemberRepository memberRepository;

	// **생성자 주입을 통한 의존성 주입**
	public QuizRoomService(MemberCommand memberCommand, QuizQuery quizQuery, QuizCommand quizCommand,
			SessionRepository sessionRepository, QuizRoomRepository quizRoomRepository, ChatService chatService,
			QuizRoomAttendeeRepository quizRoomAttendeeRepository, QuizMusicRepository quizMusicRepository,
			MemberGameStatsRepository memberGameStatsRepository) {  // memberGameStatsRepository 추가
		this.memberCommand = memberCommand;
		this.quizQuery = quizQuery;
		this.quizCommand = quizCommand;
		this.sessionRepository = sessionRepository;
		this.quizRoomRepository = quizRoomRepository;
		this.chatService = chatService;
		this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
		this.quizMusicRepository = quizMusicRepository;
		this.memberGameStatsRepository = memberGameStatsRepository;
	}

	// **게임 통계 초기화 메서드**: 멤버의 게임 통계가 없을 경우 생성합니다.
	private void ensureMemberGameStats(Member member) {
	    // Member ID에 해당하는 통계를 가져오거나 새로 생성
	    MemberGameStats stats = memberGameStatsRepository.findByMemberId(member.getMemberId());
	    
	    if (stats == null) {
	        // 멤버의 ID를 사용해 새로운 통계를 생성하고 저장
	        MemberGameStats newStats = new MemberGameStats(member.getMemberId());
	        memberGameStatsRepository.save(newStats);
	    }
	}

	// **퀴즈 방 목록 조회 (페이지네이션 지원)**
	@Transactional(readOnly = true)
	public QuizRoomListBean getAllQuizRooms(Pageable pageable) {
		Page<QuizRoom> rooms = quizQuery.findQuizRoomByPageable(pageable);

		if (rooms.isEmpty()) {
			throw new CustomException(StatusCode.NOT_EXIST_ROOMS, "퀴즈 방이 존재하지 않습니다.");
		}

		List<QuizRoomBean> quizRoomList = new ArrayList<>();
		rooms.forEach(room -> {
			// 퀴즈 타입이 유효한지 확인
			String quizQuestionType = getValidQuizType(room.getQuizRoomType());

			quizRoomList.add(new QuizRoomBean(room.getQuizRoomId(), room.getQuizRoomName(), room.getQuizRoomPassword(),
					room.getOwner(), room.getStatus(), room.getMemberCount(), room.getMaxCapacity(), room.getMaxMusic(),
					quizQuestionType));
		});

		return new QuizRoomListBean(rooms.getTotalPages(), quizRoomList);
	}

	// **유효한 퀴즈 타입 반환 메서드**
	private String getValidQuizType(String quizRoomType) {
		if ("노래 제목 맞히기".equals(quizRoomType) || "가수 이름 맞히기".equals(quizRoomType)) {
			return quizRoomType;
		} else {
			return "노래 제목 맞히기"; // 기본값 설정
		}
	}

	@Transactional
	public QuizRoomBean createRoom(QuizRoomBean quizRoomBean, MemberBean memberBean) {
		// 멤버 조회 및 게임 통계 업데이트
		int memberId = memberBean.getMember_id(); // MemberBean에서 ID만 가져옴
		Member member = memberCommand.findMemberById(memberId); // memberId로 멤버 조회
		ensureMemberGameStats(member);

		// 게임 통계 업데이트
		MemberGameStats stats = memberGameStatsRepository.findByMemberId(member.getMemberId());
		stats.setMakeRoomNum(stats.getMakeRoomNum() + 1);  // 게임 통계 업데이트
		memberGameStatsRepository.save(stats);  // 변경 사항 저장

		// 방 타입 설정
		String quizRoomType = quizRoomBean.getQuizRoomType();
		if (quizRoomType == null || quizRoomType.isBlank()) {
			quizRoomType = "songTitle";
		}

		// 방 객체 생성 및 저장
		QuizRoom quizRoom = new QuizRoom(quizRoomBean.getQuizRoomName(), quizRoomBean.getQuizRoomPassword(),
				member.getNickname(), 1, 1, quizRoomBean.getMaxCapacity(), quizRoomBean.getMaxMusic(), quizRoomType);
		quizCommand.saveQuizRoom(quizRoom);
		quizRoomRepository.flush();

		// 랜덤 퀴즈 가져오기
		int maxSongs = quizRoomBean.getMaxMusic();
		List<QuizMusic> selectedQuizzes = quizMusicRepository.findRandomQuizzes(maxSongs);

		// 새로운 QuizMusic 객체로 저장
		selectedQuizzes.forEach(quiz -> {
			QuizMusic newQuiz = new QuizMusic();
			newQuiz.setQuizId(quizRoom.getQuizRoomId()); // 방 ID 매핑
			newQuiz.setMusicId(quiz.getMusicId());
			newQuiz.setName(quiz.getName());
			newQuiz.setCode(quiz.getCode());
			newQuiz.setStart(quiz.getStart());
			newQuiz.setTags(quiz.getTags());
			newQuiz.setAnswer(quiz.getAnswer());

			quizMusicRepository.save(newQuiz); // 새 엔티티로 저장
		});

		// 방 참여자 정보 저장
		quizCommand.saveQuizRoomAttendee(new QuizRoomAttendee(quizRoom, member));

		return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
				quizRoom.getOwner(), quizRoom.getStatus(), quizRoom.getMemberCount(), quizRoom.getMaxCapacity(),
				quizRoom.getMaxMusic(), quizRoom.getQuizRoomType());
	}

	// **방 검색 기능 (페이지네이션 + 키워드 검색)**
	@Transactional
	public QuizRoomListBean searchQuizRoom(Pageable pageable, String keyword) {
		var rooms = quizRoomRepository.findByQuizRoomNameContaining(pageable, keyword);
		if (rooms.isEmpty()) {
			throw new CustomException(StatusCode.NOT_EXIST_ROOMS);
		}

		List<QuizRoomBean> quizRoomList = new ArrayList<>();
		rooms.forEach(room -> {
			quizRoomList.add(new QuizRoomBean(room.getQuizRoomId(), room.getQuizRoomName(), room.getQuizRoomPassword(),
					room.getOwner(), room.getStatus(), room.getMemberCount(), room.getMaxCapacity(), room.getMaxMusic(),
					room.getQuizRoomType()));
		});

		return new QuizRoomListBean(rooms.getTotalPages(), quizRoomList);
	}

	// **퀴즈 방 입장 처리**
	@Transactional
	public Map<String, String> enterQuizRoom(int roomId, MemberBean memberBean, String roomPassword) {
		try {
			System.out.println("퀴즈 방 입장 시도: roomId=" + roomId + ", memberId=" + memberBean.getMember_id());

			// Member 조회
			Member member = memberCommand.findMemberById(memberBean.getMember_id());
			System.out.println("회원 조회 성공: memberId=" + member.getMemberId());

			// QuizRoom 조회 및 잠금
			QuizRoom quizRoom = quizQuery.findQuizRoomByRoomIdLock(roomId);
			System.out.println("퀴즈 방 조회 성공: quizRoomId=" + quizRoom.getQuizRoomId());

			// 참가자 목록 조회
			List<QuizRoomAttendee> attendees = quizQuery.findAttendeeByQuizRoom(quizRoom);
			System.out.println("참가자 목록 조회 성공: " + attendees.size() + "명");

			// 중복 참여 확인
			if (attendees.stream().anyMatch(att -> att.getMember().getMemberId() == member.getMemberId())) {
				throw new CustomException(StatusCode.MEMBER_DUPLICATED, "이미 방에 참여 중입니다.");
			}

			// 방 비밀번호 확인
			String quizRoomPassword = quizRoom.getQuizRoomPassword() != null ? quizRoom.getQuizRoomPassword() : "";
			if (!quizRoomPassword.isEmpty() && !quizRoomPassword.equals(roomPassword)) {
				throw new CustomException(StatusCode.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
			}

			// Member 게임 상태 업데이트
			ensureMemberGameStats(member);
			MemberGameStats stats = memberGameStatsRepository.findByMemberId(member.getMemberId());
			stats.setEnterGameNum(stats.getEnterGameNum() + 1);
			memberGameStatsRepository.save(stats);

			// QuizRoomAttendee 추가
			quizCommand.saveQuizRoomAttendee(new QuizRoomAttendee(quizRoom, member));

			// 퀴즈 방 인원 수 증가
			quizRoom.setMemberCount(quizRoom.getMemberCount() + 1);
			quizCommand.saveQuizRoom(quizRoom);

			// 성공 응답 반환
			return Map.of("quizRoomName", quizRoom.getQuizRoomName(), "roomId",
					String.valueOf(quizRoom.getQuizRoomId()), "owner", quizRoom.getOwner(), "status",
					String.valueOf(quizRoom.getStatus()));
		} catch (CustomException e) {
			System.err.println("CustomException 발생: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("예상치 못한 오류 발생: " + e.getMessage());
			e.printStackTrace();
			throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, "퀴즈 방 입장 중 문제가 발생했습니다.");
		}
	}

	// **일반 채팅 메시지 처리**
	public void processChatMessage(int roomId, String sender, String content) {
		chatService.sendChatMessage(roomId, sender, content);
	}

	@Transactional
	public void roomExit(int roomId, MemberBean memberBean) throws Exception {
		// 1. 방 정보 가져오기
		Optional<QuizRoom> roomOptional = quizRoomRepository.findById(roomId);
		if (!roomOptional.isPresent()) {
			throw new CustomException("해당 방이 존재하지 않습니다.");
		}

		QuizRoom room = roomOptional.get();

		// 2. 현재 방에 남아있는 멤버 수 확인
		List<QuizRoomAttendee> roomAttendees = room.getAttendees();
		roomAttendees.removeIf(attendee -> attendee.getMember().getMemberId() == memberBean.getMember_id());

		if (roomAttendees.isEmpty()) {
			// 3. 방에 아무도 없으면 방 삭제
			quizRoomRepository.delete(room);
			log.info("방을 삭제했습니다. 방 ID: {}", roomId);
		} else {
			// 4. 방에 남아있는 사람이 있으면 방장을 넘겨줌
			if (room.getOwner().equals(memberBean.getNickname())) {
				QuizRoomAttendee newOwnerAttendee = roomAttendees.get(0);
				room.setOwner(newOwnerAttendee.getMember().getNickname());
				quizRoomRepository.save(room);
				log.info("방장 권한을 {}님에게 넘겼습니다.", newOwnerAttendee.getMember().getNickname());
			}
		}
	}

	// **방 ID로 퀴즈 방 조회**
	@Transactional(readOnly = true)
	public QuizRoomBean findRoomById(int roomId) {
		QuizRoom quizRoom = quizQuery.findQuizRoomByRoomId(roomId);
		if (quizRoom == null) {
			throw new CustomException(StatusCode.NOT_EXIST_ROOMS);
		}

		List<QuizRoomAttendee> attendees = quizRoomAttendeeRepository.findByQuizRoom(quizRoom);
		int memberCount = attendees.size();

		return new QuizRoomBean(quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
				quizRoom.getOwner(), quizRoom.getStatus(), memberCount, quizRoom.getMaxCapacity(),
				quizRoom.getMaxMusic(), quizRoom.getQuizRoomType());
	}

	// **참가자들의 닉네임 목록 가져오기**
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
}
