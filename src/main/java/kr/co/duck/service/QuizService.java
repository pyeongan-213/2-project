package kr.co.duck.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.duck.domain.Member;
import kr.co.duck.domain.MemberGameStats;
import kr.co.duck.domain.QuizMessage;
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizRoomAttendee;
import kr.co.duck.repository.MemberGameStatsRepository;
import kr.co.duck.repository.MemberRepository;
import kr.co.duck.repository.QuizMusicRepository;
import kr.co.duck.repository.QuizRoomAttendeeRepository;
import kr.co.duck.util.CustomException;
import kr.co.duck.util.StatusCode;

@Service
public class QuizService {

    private final QuizMusicRepository quizMusicRepository;
    private final MemberGameStatsRepository memberGameStatsRepository;
    private final MemberRepository memberRepository;
    private final QuizRoomAttendeeRepository quizRoomAttendeeRepository;
    private final ChatService chatService;
    private final Random random = new Random();

    @Autowired
    private ServletContext servletContext;

    @Autowired
    public QuizService(QuizMusicRepository quizMusicRepository,
                       MemberGameStatsRepository memberGameStatsRepository,
                       MemberRepository memberRepository,
                       QuizRoomAttendeeRepository quizRoomAttendeeRepository,
                       ChatService chatService) {
        this.quizMusicRepository = quizMusicRepository;
        this.memberGameStatsRepository = memberGameStatsRepository;
        this.memberRepository = memberRepository;
        this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
        this.chatService = chatService;
    }

    // **서버 시작 시 JSON 데이터를 DB에 로드**
    @PostConstruct
    public void init() {
        loadJsonToDatabase();
    }

    @Transactional
    public void loadJsonToDatabase() {
        try {
            // JSON 파일의 절대 경로 가져오기
            String filePath = servletContext.getRealPath("/resources/quizData/music_kr.json");
            System.out.println("JSON 파일 경로: " + filePath);

            File jsonFile = new File(filePath);
            if (!jsonFile.exists()) {
                throw new RuntimeException("JSON 파일을 찾을 수 없습니다: " + filePath);
            }

            // JSON 파일을 읽어 리스트로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            List<QuizMusic> quizMusicList = objectMapper.readValue(
                    new FileInputStream(jsonFile), new TypeReference<List<QuizMusic>>() {});

            // 중복을 피하고 새로운 데이터만 저장하는 로직
            for (QuizMusic quizMusic : quizMusicList) {
                boolean exists = quizMusicRepository.existsByQuizIdAndMusicId(
                    quizMusic.getQuizId(), quizMusic.getMusicId());

                if (!exists) {
                    quizMusicRepository.save(quizMusic); // 중복이 없을 경우에만 저장
                    System.out.println("새로운 데이터 저장: " + quizMusic.getName());
                } else {
                    System.out.println("이미 존재하는 데이터: " + quizMusic.getName());
                }
            }

            System.out.println("데이터 로드 완료.");
        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("JSON 파일 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public QuizMusic getRandomQuizQuestion(int quizId) {
        List<QuizMusic> quizList;

        try {
            quizList = quizMusicRepository.findByQuizId(quizId);
            System.out.println("조회된 퀴즈 개수: " + quizList.size());  // 로그 추가
        } catch (Exception e) {
        	System.err.println("퀴즈 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // 상세 오류 정보 출력
            throw new CustomException(StatusCode.INVALID_TOKEN, "퀴즈 조회 중 오류가 발생했습니다.");
        }

        if (quizList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "해당 퀴즈에 문제가 없습니다.");
        }

        int randomIndex = random.nextInt(quizList.size());
        QuizMusic randomQuiz = quizList.get(randomIndex);
        System.out.println("선택된 퀴즈: " + randomQuiz);  // 로그 추가

        return randomQuiz;
    }

    @Transactional
    public void createQuiz(QuizMusic quizMusic) {
        if (quizMusic == null) {
            throw new IllegalArgumentException("퀴즈 정보가 유효하지 않습니다.");
        }
        quizMusicRepository.save(quizMusic);
    }

    @Transactional(readOnly = true)
    public List<QuizMusic> getAllQuizzes() {
        return quizMusicRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<QuizMusic> getQuizByQuizId(int quizId) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        if (quizList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ);
        }
        return quizList;
    }

    @Transactional
    public void deleteQuiz(int quizId) {
        List<QuizMusic> quizMusicList = quizMusicRepository.findByQuizId(quizId);
        if (quizMusicList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "삭제할 퀴즈가 없습니다.");
        }
        quizMusicRepository.deleteAll(quizMusicList);
    }

    @Transactional
    public void quizStart(int roomId) {
        List<QuizRoomAttendee> attendees = quizRoomAttendeeRepository.findByQuizRoomQuizRoomId(roomId);
        if (attendees.isEmpty()) {
            throw new CustomException(StatusCode.NOT_ENOUGH_MEMBER);
        }

        Map<String, Object> contentSet = new HashMap<>();
        contentSet.put("alert", "퀴즈가 시작되었습니다!");
        contentSet.put("participants", getNicknameList(attendees));

        chatService.sendQuizMessage(roomId, QuizMessage.MessageType.START, contentSet, null);
    }

    @Transactional
    public boolean submitAnswer(int memberId, int quizId, String userAnswer) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);

        boolean isCorrect = quizList.stream().anyMatch(quiz -> {
            List<String> answers = quiz.getAnswer();  // List로 변환된 answer 사용
            return answers.stream().anyMatch(a -> a.equalsIgnoreCase(userAnswer));
        });

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_MEMBER));

        updateGameStats(member.getMemberGameStats(), isCorrect);
        memberGameStatsRepository.save(member.getMemberGameStats());

        QuizMessage.MessageType messageType = isCorrect 
            ? QuizMessage.MessageType.CORRECT 
            : QuizMessage.MessageType.INCORRECT;

        String messageContent = isCorrect ? "정답입니다!" : "오답입니다!";
        chatService.sendQuizMessage(quizId, messageType, messageContent, member.getNickname());

        return isCorrect;
    }


    private void updateGameStats(MemberGameStats stats, boolean isCorrect) {
        if (isCorrect) {
            stats.setWinNum(stats.getWinNum() + 1);
        } else {
            stats.setLoseNum(stats.getLoseNum() + 1);
        }
        stats.setTotalGameNum(stats.getTotalGameNum() + 1);
        stats.setPlayTime(stats.getPlayTime() + calculatePlayTime());
    }

    public List<String> getNicknameList(List<QuizRoomAttendee> attendees) {
        List<String> nicknameList = new ArrayList<>();
        for (QuizRoomAttendee attendee : attendees) {
            nicknameList.add(attendee.getMemberNickname());
        }
        return nicknameList;
    }

    private int calculatePlayTime() {
        return 60;
    }
}
