package kr.co.duck.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.duck.controller.ChatController;
import kr.co.duck.domain.Member;
import kr.co.duck.domain.MemberGameStats;
import kr.co.duck.domain.QuizMessage;
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizRoom;
import kr.co.duck.domain.QuizRoomAttendee;
import kr.co.duck.repository.MemberGameStatsRepository;
import kr.co.duck.repository.MemberRepository;
import kr.co.duck.repository.QuizMusicRepository;
import kr.co.duck.repository.QuizRoomAttendeeRepository;
import kr.co.duck.repository.QuizRoomRepository;
import kr.co.duck.util.CustomException;
import kr.co.duck.util.StatusCode;

@Service
public class QuizService {

    // 필드 정의
    private final QuizMusicRepository quizMusicRepository;
    private final MemberGameStatsRepository memberGameStatsRepository;
    private final MemberRepository memberRepository;
    private final QuizRoomAttendeeRepository quizRoomAttendeeRepository;
    private final ChatService chatService;
    private final QuizRoomRepository quizRoomRepository;
    private final Random random = new Random();
    private final Map<Integer, Integer> hintProgress = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 전송을 위한 템플릿
    @Autowired
    private ServletContext servletContext;

    @Autowired
    public QuizService(
        QuizMusicRepository quizMusicRepository,
        MemberGameStatsRepository memberGameStatsRepository,
        MemberRepository memberRepository,
        QuizRoomAttendeeRepository quizRoomAttendeeRepository,
        ChatService chatService,
        QuizRoomRepository quizRoomRepository
    ) {
        this.quizMusicRepository = quizMusicRepository;
        this.memberGameStatsRepository = memberGameStatsRepository;
        this.memberRepository = memberRepository;
        this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
        this.chatService = chatService;
        this.quizRoomRepository = quizRoomRepository;
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
                    new FileInputStream(jsonFile),
                    new TypeReference<List<QuizMusic>>() {});

            for (QuizMusic quizMusic : quizMusicList) {
                boolean exists = quizMusicRepository.existsByMusicId(quizMusic.getMusicId());
                if (!exists) {
                    quizMusicRepository.save(quizMusic);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("JSON 파일 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        System.out.println("뮤직 데이터 DB 저장 완료");
    }

    
    @Transactional(readOnly = true)
    public List<QuizMusic> getQuizByQuizId(int quizId) {
        List<QuizMusic> quizMusicList = quizMusicRepository.findByQuizId(quizId);
        if (quizMusicList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "해당 퀴즈를 찾을 수 없습니다.");
        }
        return quizMusicList;
    }

    @Transactional(readOnly = true)
    public QuizMusic getRandomQuizQuestion(int quizId, String quizType) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        if (quizList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "해당 퀴즈에 문제가 없습니다.");
        }

        QuizMusic quizMusic = quizList.get(random.nextInt(quizList.size()));
        String[] nameParts = quizMusic.getName() != null ?
                             quizMusic.getName().split(" - ") : new String[]{"알 수 없음"};
        String songTitle = nameParts[0].trim();
        String artistName = nameParts.length > 1 ? nameParts[1].trim() : "알 수 없음";

        String normalizedQuizType = quizType.trim().replaceAll("\\s+", "").toLowerCase();
        switch (normalizedQuizType) {
            case "artistname":
            case "가수이름맞히기":
                quizMusic.setAnswer(Collections.singletonList(artistName));
                break;
            case "songtitle":
            case "노래제목맞히기":
                quizMusic.setAnswer(Collections.singletonList(songTitle));
                break;
            default:
                throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 퀴즈 유형입니다.");
        }

        return quizMusic;
    }

    public String getQuizTypeForRoom(int roomId) {
        QuizRoom quizRoom = quizRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_EXIST_ROOMS, "해당 방이 존재하지 않습니다."));
        String quizType = quizRoom.getQuizRoomType();
        if (quizType == null || quizType.isBlank()) {
            quizType = "songTitle";
        }
        return quizType;
    }

    @Transactional
    public void startNextQuiz(int roomId) {
        String quizType = getQuizTypeForRoom(roomId);
        QuizMusic nextQuiz = getRandomQuizQuestion(roomId, quizType);

        if (nextQuiz != null) {
            chatService.sendQuizMessage(roomId, QuizMessage.MessageType.NEXT, nextQuiz, "시스템");
        }
    }

    @Transactional
    public boolean submitAnswer(int memberId, int quizId, String userAnswer) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        String normalizedUserAnswer = userAnswer.replaceAll("\\s+", "").toLowerCase();

        boolean isCorrect = quizList.stream().anyMatch(quiz ->
                quiz.getAnswer().stream()
                        .anyMatch(answer -> 
                                answer.replaceAll("\\s+", "").equalsIgnoreCase(normalizedUserAnswer)
                        )
        );

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_MEMBER));

        MemberGameStats stats = ensureMemberGameStats(member.getMemberId());
        updateGameStats(stats, isCorrect);

        chatService.sendQuizMessage(quizId,
                isCorrect ? QuizMessage.MessageType.CORRECT : QuizMessage.MessageType.INCORRECT,
                isCorrect ? "정답입니다!" : "오답입니다!", member.getNickname());

        return isCorrect;
    }

    private MemberGameStats ensureMemberGameStats(int memberId) {
        MemberGameStats stats = memberGameStatsRepository.findByMemberId(memberId);
        
        if (stats == null) {
            // 멤버의 ID를 사용해 새로운 통계를 생성하고 저장
            MemberGameStats newStats = new MemberGameStats(memberId);
            memberGameStatsRepository.save(newStats);
            return newStats;  // 새로 생성한 통계 반환
        }

        return stats;  // 기존 통계 반환
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
    
    public String generateHint(int quizId) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        
        if (quizList.isEmpty()) {
            log.warn("퀴즈 ID {}에 대한 퀴즈 데이터를 찾을 수 없습니다.", quizId);
            return "힌트를 제공할 수 없습니다."; // 퀴즈 음악이 없는 경우
        }

        // 첫 번째 퀴즈 음악의 첫 번째 정답을 사용
        String answer = quizList.get(0).getAnswer().get(0);
        log.info("퀴즈 ID {}에 대한 정답: {}", quizId, answer); 

        // 힌트 길이를 업데이트
        int hintLength = hintProgress.getOrDefault(quizId, 0) + 1;
        if (hintLength > answer.length()) {
            hintLength = answer.length(); // 힌트가 정답 길이를 초과하지 않도록 설정
        }

        // 힌트 생성
        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            if (i < hintLength) {
                hintBuilder.append(answer.charAt(i)); // 힌트를 제공할 부분
            } else {
                hintBuilder.append('○'); // 아직 제공되지 않은 부분은 마스킹 처리
            }
        }

        // 현재 힌트 길이를 저장하여 다음 힌트 제공 시 사용
        hintProgress.put(quizId, hintLength);
        
        String generatedHint = hintBuilder.toString();
        log.info("퀴즈 ID {}에 대해 생성된 힌트: {}", quizId, generatedHint);

        // **WebSocket을 통해 실시간 힌트 전송**
        Map<String, Object> hintMessage = new HashMap<>();
        hintMessage.put("hint", generatedHint);
        hintMessage.put("quizId", quizId);
        
        // 모든 참가자에게 힌트를 전송
        messagingTemplate.convertAndSend("/sub/quiz/" + quizId + "/hintMessage", hintMessage);
        
        return generatedHint;
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

 // getSongName 메서드 수정
    public String getSongName(int quizId) {
        // quizId로 QuizMusic 엔티티를 조회합니다.
        QuizMusic quizMusic = quizMusicRepository.findByQuizId(quizId)
            .stream()
            .findFirst() // 첫 번째 결과만 사용 (여러 개가 반환될 수 있으므로)
            .orElseThrow(() -> new CustomException("퀴즈를 찾을 수 없습니다: " + quizId));
        
        return quizMusic.getName(); // 실제로 노래 제목을 저장한 필드
    }
    
    @Transactional
    public void deleteQuiz(int quizId) {
        List<QuizMusic> quizMusicList = quizMusicRepository.findByQuizId(quizId);
        if (quizMusicList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "삭제할 퀴즈가 없습니다.");
        }
        quizMusicRepository.deleteAll(quizMusicList);
    }
}
