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

    // 힌트 진행 상태 관리
    private final Map<Integer, Integer> hintProgress = new HashMap<>();

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

    @PostConstruct
    public void init() {
        loadJsonToDatabase();
    }

    @Transactional
    public void loadJsonToDatabase() {
        try {
            String filePath = servletContext.getRealPath("/resources/quizData/music_kr.json");
            File jsonFile = new File(filePath);

            if (!jsonFile.exists()) {
                throw new RuntimeException("JSON 파일을 찾을 수 없습니다: " + filePath);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<QuizMusic> quizMusicList = objectMapper.readValue(
                    new FileInputStream(jsonFile), new TypeReference<List<QuizMusic>>() {});

            for (QuizMusic quizMusic : quizMusicList) {
                boolean exists = quizMusicRepository.existsByQuizIdAndMusicId(
                        quizMusic.getQuizId(), quizMusic.getMusicId());

                if (!exists) {
                    quizMusicRepository.save(quizMusic);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("JSON 파일 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public QuizMusic getRandomQuizQuestion(int quizId) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        if (quizList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "해당 퀴즈에 문제가 없습니다.");
        }
        return quizList.get(random.nextInt(quizList.size()));
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
        return quizMusicRepository.findByQuizId(quizId);
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
    public boolean submitAnswer(int memberId, int quizId, String userAnswer) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        boolean isCorrect = quizList.stream().anyMatch(quiz ->
                quiz.getAnswer().stream().anyMatch(answer ->
                        answer.replaceAll("\\s+", "").equalsIgnoreCase(userAnswer.replaceAll("\\s+", ""))));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_MEMBER));

        updateGameStats(member.getMemberGameStats(), isCorrect);

        chatService.sendQuizMessage(quizId,
                isCorrect ? QuizMessage.MessageType.CORRECT : QuizMessage.MessageType.INCORRECT,
                isCorrect ? "정답입니다!" : "오답입니다!", member.getNickname());

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

    private int calculatePlayTime() {
        return 60;
    }

    public List<String> getNicknameList(List<QuizRoomAttendee> attendees) {
        List<String> nicknameList = new ArrayList<>();
        attendees.forEach(attendee -> nicknameList.add(attendee.getMemberNickname()));
        return nicknameList;
    }

    /**
     * 힌트 생성 메서드: 요청 시 정답의 일부를 점진적으로 공개합니다.
     * @param quizId 퀴즈 ID
     * @return 생성된 힌트 문자열
     */
    public String generateHint(int quizId) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        if (quizList.isEmpty()) {
            return "힌트를 제공할 수 없습니다.";
        }

        String answer = quizList.get(0).getAnswer().get(0); // 첫 번째 정답 사용
        int hintLength = hintProgress.getOrDefault(quizId, 0) + 1;

        // 힌트 생성 (공개된 부분은 문자로, 나머지는 'O'로 표시)
        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            if (i < hintLength) {
                hintBuilder.append(answer.charAt(i));
            } else {
                hintBuilder.append('○');
            }
        }

        // 힌트 진행 상태 업데이트
        hintProgress.put(quizId, hintLength);
        return hintBuilder.toString();
    }
}
