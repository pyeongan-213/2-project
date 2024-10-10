package kr.co.duck.service;

import static kr.co.duck.util.StatusCode.QUIZ_NOT_FOUND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.duck.beans.QuizBean;
import kr.co.duck.domain.Member;
import kr.co.duck.domain.MemberGameStats;
import kr.co.duck.domain.QuizMessage;
import kr.co.duck.domain.QuizRoomAttendee;
import kr.co.duck.repository.MemberGameStatsRepository;
import kr.co.duck.repository.MemberRepository;
import kr.co.duck.repository.QuizRepository;
import kr.co.duck.repository.QuizRoomAttendeeRepository;
import kr.co.duck.util.CustomException;
import kr.co.duck.util.StatusCode;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final MemberGameStatsRepository memberGameStatsRepository;
    private final MemberRepository memberRepository;
    private final QuizRoomAttendeeRepository quizRoomAttendeeRepository;
    private final ChatService chatService; // ChatService 추가

    // 생성자
    @Autowired
    public QuizService(QuizRepository quizRepository, MemberGameStatsRepository memberGameStatsRepository, 
                       MemberRepository memberRepository, QuizRoomAttendeeRepository quizRoomAttendeeRepository,
                       ChatService chatService) {  // ChatService 주입
        this.quizRepository = quizRepository;
        this.memberGameStatsRepository = memberGameStatsRepository;
        this.memberRepository = memberRepository;
        this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
        this.chatService = chatService;  // ChatService 초기화
    }

    // 퀴즈 생성
    @Transactional
    public void createQuiz(QuizBean quizBean) {
        quizRepository.save(quizBean);
    }

    // 모든 퀴즈 가져오기
    @Transactional(readOnly = true)
    public List<QuizBean> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // 특정 퀴즈 가져오기
    @Transactional(readOnly = true)
    public QuizBean getQuiz(int quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(QUIZ_NOT_FOUND));
    }

    // 퀴즈 업데이트
    @Transactional
    public void updateQuiz(int quizId, QuizBean quizBean) {
        QuizBean existingQuiz = getQuiz(quizId);
        existingQuiz.setQuizTitle(quizBean.getQuizTitle());
        existingQuiz.setQuizText(quizBean.getQuizText());
        quizRepository.save(existingQuiz);
    }

    // 퀴즈 삭제
    @Transactional
    public void deleteQuiz(int quizId) {
        quizRepository.deleteById(quizId);
    }

 // 퀴즈 시작
    @Transactional
    public void quizStart(int roomId, QuizBean quizBean) {
        // 퀴즈방에 있는 사용자 정보를 가져오는 로직
        List<QuizRoomAttendee> attendees = quizRoomAttendeeRepository.findByQuizRoomQuizRoomId(roomId);

        Map<String, Object> contentSet = new HashMap<>();
        contentSet.put("alert", "퀴즈가 시작되었습니다!");

        // 참가자 정보를 추가 (예: 참가자 목록)
        List<String> nicknameList = getNicknameList(attendees);
        contentSet.put("participants", nicknameList);

        // WebSocket 메시지 전송 (ChatService 사용)
        chatService.sendQuizMessage(roomId, QuizMessage.MessageType.START, contentSet, null);
    }

 // 강제 종료 로직
    @Transactional
    public void forcedEndQuiz(int roomId, String nickname) {
        Map<String, Object> contentSet = new HashMap<>();
        contentSet.put("alert", nickname + "님에 의해 퀴즈가 강제 종료되었습니다!");

        // WebSocket 메시지 전송 (messageType 추가)
        chatService.sendQuizMessage(roomId, QuizMessage.MessageType.FORCEDENDQUIZ, "퀴즈가 강제 종료되었습니다!", null);
    }


    // 정답 제출 및 승패 기록 업데이트
    @Transactional
    public boolean submitAnswer(int memberId, QuizBean quizBean) {
        QuizBean existingQuiz = getQuiz(quizBean.getQuizId());
        boolean isCorrect = existingQuiz.getQuizAnswer().equalsIgnoreCase(quizBean.getQuizAnswer());

        // Member 정보를 가져와 닉네임을 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_MEMBER));

        MemberGameStats stats = member.getMemberGameStats();

        // 정답 여부에 따라 승리/패배 횟수 업데이트
        if (isCorrect) {
            stats.setWinNum(stats.getWinNum() + 1);
        } else {
            stats.setLoseNum(stats.getLoseNum() + 1);
        }

        // 총 게임 횟수와 플레이 시간 업데이트
        stats.setTotalGameNum(stats.getTotalGameNum() + 1);
        stats.setPlayTime(stats.getPlayTime() + calculatePlayTime());

        // 승패 기록을 저장
        memberGameStatsRepository.save(stats);

     // 정답 여부에 따른 메시지 전송
        QuizMessage.MessageType messageType = isCorrect ? QuizMessage.MessageType.CORRECT : QuizMessage.MessageType.INCORRECT;
        String messageContent = isCorrect ? "정답입니다!" : "오답입니다!";

        chatService.sendQuizMessage(quizBean.getQuizId(), messageType, messageContent, member.getNickname());

        return isCorrect;
    }

    // 참가자와 퀴즈 매칭
    public Map<String, String> matchQuizToMember(List<QuizBean> quizList, List<String> memberNicknameList) {
        Map<String, String> quizToMember = new HashMap<>();

        for (int i = 0; i < quizList.size(); i++) {
            quizToMember.put(memberNicknameList.get(i), quizList.get(i).getQuizTitle());
        }

        return quizToMember;
    }

    // 방의 참가자들 닉네임 가져오기
    public List<String> getNicknameList(List<QuizRoomAttendee> attendees) {
        List<String> nicknameList = new ArrayList<>();
        for (QuizRoomAttendee attendee : attendees) {
            nicknameList.add(attendee.getMemberNickname());
        }
        return nicknameList;
    }

    // 플레이 시간 계산 예시 메서드
    private int calculatePlayTime() {
        return 60; // 예시로 60초 플레이했다고 가정
    }
}
