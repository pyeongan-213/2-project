package kr.co.duck.service;

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
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizQuery;
import kr.co.duck.domain.QuizRoom;
import kr.co.duck.domain.QuizRoomAttendee;
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

    
    
    // **생성자 주입을 통한 의존성 주입**
    public QuizRoomService(MemberCommand memberCommand, QuizQuery quizQuery, QuizCommand quizCommand,
                           SessionRepository sessionRepository, QuizRoomRepository quizRoomRepository,
                           ChatService chatService, QuizRoomAttendeeRepository quizRoomAttendeeRepository, QuizMusicRepository quizMusicRepository) {
        this.memberCommand = memberCommand;
        this.quizQuery = quizQuery;
        this.quizCommand = quizCommand;
        this.sessionRepository = sessionRepository;
        this.quizRoomRepository = quizRoomRepository;
        this.chatService = chatService;
        this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
        this.quizMusicRepository = quizMusicRepository;
    }

    // **게임 통계 초기화 메서드**: 멤버의 게임 통계가 없을 경우 생성합니다.
    private void ensureMemberGameStats(Member member) {
        if (member.getMemberGameStats() == null) {
            member.setMemberGameStats(new MemberGameStats(member));
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

            quizRoomList.add(new QuizRoomBean(
                    room.getQuizRoomId(), room.getQuizRoomName(), room.getQuizRoomPassword(),
                    room.getOwner(), room.getStatus(), room.getMemberCount(),
                    room.getMaxCapacity(), room.getMaxMusic(), quizQuestionType));
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
        Member member = memberCommand.findMemberById(memberBean.getMember_id());
        ensureMemberGameStats(member);

        member.getMemberGameStats().setMakeRoomNum(member.getMemberGameStats().getMakeRoomNum() + 1);
        memberCommand.saveMember(member);

    

        String quizRoomType = quizRoomBean.getQuizRoomType();
        if (quizRoomType == null || quizRoomType.isBlank()) { 
            quizRoomType = "songTitle";
        }

        // 방 객체 생성 및 저장
        QuizRoom quizRoom = new QuizRoom(
                quizRoomBean.getQuizRoomName(), quizRoomBean.getQuizRoomPassword(),
                member.getNickname(), 1, 1,
                quizRoomBean.getMaxCapacity(), quizRoomBean.getMaxMusic(), quizRoomType
        );
        quizCommand.saveQuizRoom(quizRoom);
        quizRoomRepository.flush();



        // 랜덤 퀴즈 가져오기 (기존 엔티티를 수정하지 않고 새로운 엔티티로 저장)
        int maxSongs = quizRoomBean.getMaxMusic();
        List<QuizMusic> selectedQuizzes = quizMusicRepository.findRandomQuizzes(maxSongs);

        selectedQuizzes.forEach(quiz -> {
            // 새로운 QuizMusic 객체 생성 (기존 엔티티 수정 방지)
            QuizMusic newQuiz = new QuizMusic();
            newQuiz.setQuizId(quizRoom.getQuizRoomId());  // 방 ID 매핑
            newQuiz.setMusicId(quiz.getMusicId());
            newQuiz.setName(quiz.getName());
            newQuiz.setCode(quiz.getCode());
            newQuiz.setStart(quiz.getStart());
            newQuiz.setTags(quiz.getTags());
            newQuiz.setAnswer(quiz.getAnswer());

            quizMusicRepository.save(newQuiz);  // 새 엔티티로 저장
        });

        quizCommand.saveQuizRoomAttendee(new QuizRoomAttendee(quizRoom, member));

        return new QuizRoomBean(
                quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
                quizRoom.getOwner(), quizRoom.getStatus(), quizRoom.getMemberCount(),
                quizRoom.getMaxCapacity(), quizRoom.getMaxMusic(), quizRoom.getQuizRoomType()
        );
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
            quizRoomList.add(new QuizRoomBean(
                    room.getQuizRoomId(), room.getQuizRoomName(), room.getQuizRoomPassword(),
                    room.getOwner(), room.getStatus(), room.getMemberCount(),
                    room.getMaxCapacity(), room.getMaxMusic(), room.getQuizRoomType()
            ));
        });

        return new QuizRoomListBean(rooms.getTotalPages(), quizRoomList);
    }

    // **퀴즈 방 입장 처리**
    @Transactional
    public Map<String, String> enterQuizRoom(int roomId, MemberBean memberBean, String roomPassword) {
        Member member = memberCommand.findMemberById(memberBean.getMember_id());
        QuizRoom quizRoom = quizQuery.findQuizRoomByRoomIdLock(roomId);

        List<QuizRoomAttendee> attendees = quizQuery.findAttendeeByQuizRoom(quizRoom);
        if (attendees.stream().anyMatch(att -> att.getMember().getMemberId() == member.getMemberId())) {
            throw new CustomException(StatusCode.MEMBER_DUPLICATED, "이미 방에 참여 중입니다.");
        }

        String quizRoomPassword = quizRoom.getQuizRoomPassword() != null ? quizRoom.getQuizRoomPassword() : "";

        if (!quizRoomPassword.isEmpty() && !quizRoomPassword.equals(roomPassword)) {
            throw new CustomException(StatusCode.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
        }

        ensureMemberGameStats(member);
        member.getMemberGameStats().setEnterGameNum(member.getMemberGameStats().getEnterGameNum() + 1);
        memberCommand.saveMember(member);

        quizCommand.saveQuizRoomAttendee(new QuizRoomAttendee(quizRoom, member));
        quizRoom.setMemberCount(quizRoom.getMemberCount() + 1);
        quizCommand.saveQuizRoom(quizRoom);

        return Map.of(
                "quizRoomName", quizRoom.getQuizRoomName(),
                "roomId", String.valueOf(quizRoom.getQuizRoomId()),
                "owner", quizRoom.getOwner(),
                "status", String.valueOf(quizRoom.getStatus())
        );
    }

    // **일반 채팅 메시지 처리**
    public void processChatMessage(int roomId, String sender, String content) {
        chatService.sendChatMessage(roomId, sender, content);
    }

    // **방 나가기 처리**
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

    // **방 ID로 퀴즈 방 조회**
    @Transactional(readOnly = true)
    public QuizRoomBean findRoomById(int roomId) {
        QuizRoom quizRoom = quizQuery.findQuizRoomByRoomId(roomId);
        if (quizRoom == null) {
            throw new CustomException(StatusCode.NOT_EXIST_ROOMS);
        }

        List<QuizRoomAttendee> attendees = quizRoomAttendeeRepository.findByQuizRoom(quizRoom);
        int memberCount = attendees.size();

        return new QuizRoomBean(
                quizRoom.getQuizRoomId(), quizRoom.getQuizRoomName(), quizRoom.getQuizRoomPassword(),
                quizRoom.getOwner(), quizRoom.getStatus(), memberCount,
                quizRoom.getMaxCapacity(), quizRoom.getMaxMusic(), quizRoom.getQuizRoomType()
        );
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
