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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    // **필드 정의**
    private final QuizMusicRepository quizMusicRepository;  // 음악 퀴즈 레포지토리
    private final MemberGameStatsRepository memberGameStatsRepository; // 게임 통계 저장소
    private final MemberRepository memberRepository; // 멤버 정보 레포지토리
    private final QuizRoomAttendeeRepository quizRoomAttendeeRepository; // 방 참가자 정보 레포지토리
    private final ChatService chatService; // 채팅 서비스
    private final QuizRoomRepository quizRoomRepository; // 퀴즈 방 정보 레포지토리

    private final Random random = new Random(); // 랜덤 객체
    private final Map<Integer, Integer> hintProgress = new HashMap<>(); // 힌트 진행상태 저장

    @Autowired
    private ServletContext servletContext; // 서블릿 컨텍스트 객체

    // **생성자 주입**을 사용하여 필요한 의존성 주입
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

    // **PostConstruct 어노테이션**을 사용해 서비스 초기화 시 JSON 데이터를 로드
    @PostConstruct
    public void init() {
        loadJsonToDatabase();
    }

 // **JSON 파일을 데이터베이스로 로드하는 메서드**
    @Transactional
    public void loadJsonToDatabase() {
        try {
            // JSON 파일 경로 설정
            String filePath = servletContext.getRealPath("/resources/quizData/music_kr.json");
            File jsonFile = new File(filePath);

            if (!jsonFile.exists()) {
                throw new RuntimeException("JSON 파일을 찾을 수 없습니다: " + filePath);
            }

            // JSON 파일 읽기
            ObjectMapper objectMapper = new ObjectMapper();
            List<QuizMusic> quizMusicList = objectMapper.readValue(
                    new FileInputStream(jsonFile),
                    new TypeReference<List<QuizMusic>>() {});

            // 데이터베이스에 저장되지 않은 경우에만 저장
            for (QuizMusic quizMusic : quizMusicList) {
                boolean exists = quizMusicRepository.existsByMusicId(quizMusic.getMusicId());

                // 동일한 음악 ID가 존재하지 않을 경우에만 저장
                if (!exists) {
                    quizMusicRepository.save(quizMusic);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("JSON 파일 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        System.out.println("뮤직데이터 DB저장 완료");
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
        System.out.println("[INFO] 랜덤 퀴즈 요청: roomId = " + quizId);
        System.out.println("[INFO] 퀴즈 유형: " + quizType);

        // 퀴즈 목록 조회
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        System.out.println("[INFO] 조회된 퀴즈 목록 크기: " + quizList.size());

        // 퀴즈가 없는 경우 예외 처리
        if (quizList.isEmpty()) {
            System.out.println("[WARN] 퀴즈가 없습니다: quizId = " + quizId);
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "해당 퀴즈에 문제가 없습니다.");
        }

        // 랜덤한 문제 선택
        QuizMusic quizMusic = quizList.get(random.nextInt(quizList.size()));
        System.out.println("[INFO] 선택된 퀴즈: " + quizMusic);

        // 노래 제목과 가수 이름 분리
        String[] nameParts = quizMusic.getName() != null ?
                             quizMusic.getName().split(" - ") : new String[]{"알 수 없음"};
        String songTitle = nameParts[0].trim();
        String artistName = nameParts.length > 1 ? nameParts[1].trim() : "알 수 없음";

        System.out.println("[INFO] 노래 제목: " + songTitle + ", 가수 이름: " + artistName);

        // 퀴즈 유형을 정규화 (대소문자 무시, 공백 제거)
        String normalizedQuizType = quizType.trim().replaceAll("\\s+", "").toLowerCase();
        System.out.println("[INFO] 전달된 퀴즈 유형: " + quizType + " -> 정규화된 유형: " + normalizedQuizType);

        // 퀴즈 타입에 따라 정답 설정
        switch (normalizedQuizType) {
            case "artistname":
            case "가수이름맞히기":  // 한국어 유형 추가
                quizMusic.setAnswer(Collections.singletonList(artistName)); // 가수 이름 맞히기
                System.out.println("[INFO] 정답 설정: 가수 이름 (" + artistName + ")");
                break;
            case "songtitle":
            case "노래제목맞히기":  // 한국어 유형 추가
                quizMusic.setAnswer(Collections.singletonList(songTitle)); // 노래 제목 맞히기
                System.out.println("[INFO] 정답 설정: 노래 제목 (" + songTitle + ")");
                break;
            default:
                System.out.println("[ERROR] 잘못된 퀴즈 유형: " + quizType);
                throw new CustomException(StatusCode.BAD_REQUEST, "잘못된 퀴즈 유형입니다.");
        }

        return quizMusic;
    }



 // QuizService.java

    public String getQuizTypeForRoom(int roomId) {
        // DB에서 방 정보 조회
        QuizRoom quizRoom = quizRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_EXIST_ROOMS, "해당 방이 존재하지 않습니다."));

        // 조회된 quizRoomType 로그 출력
        String quizType = quizRoom.getQuizRoomType();
        System.out.println("[DEBUG] 방 ID: " + roomId + ", DB에서 조회된 quizRoomType: " + quizType);

        if (quizType == null || quizType.isBlank()) {
            System.err.println("[ERROR] quizType이 null이거나 빈 문자열입니다. 기본값 'songTitle' 사용.");
            quizType = "songTitle";
        }

        return quizType;
    }




 // QuizService.java

    @Transactional
    public void startNextQuiz(int roomId) {
        System.out.println("[INFO] 랜덤 퀴즈 요청: roomId = " + roomId);

        // 방 ID에 해당하는 퀴즈 유형 가져오기
        String quizType = getQuizTypeForRoom(roomId);
        System.out.println("[INFO] 퀴즈 유형: " + quizType);

        // 랜덤한 퀴즈 문제 가져오기
        QuizMusic nextQuiz = getRandomQuizQuestion(roomId, quizType);

        if (nextQuiz != null) {
            System.out.println("[INFO] 선택된 퀴즈: " + nextQuiz);

            // 채팅 메시지를 통해 다음 퀴즈 알림
            chatService.sendQuizMessage(roomId, QuizMessage.MessageType.NEXT, nextQuiz, "시스템");
        } else {
            System.out.println("[WARN] 다음 퀴즈를 찾을 수 없습니다.");
        }
    }


    // **퀴즈 생성 메서드**
    @Transactional
    public void createQuiz(QuizMusic quizMusic) {
        if (quizMusic == null) {
            throw new IllegalArgumentException("퀴즈 정보가 유효하지 않습니다.");
        }
        quizMusicRepository.save(quizMusic);
    }

    // **모든 퀴즈 목록 가져오기 메서드**
    @Transactional(readOnly = true)
    public List<QuizMusic> getAllQuizzes() {
        return quizMusicRepository.findAll(); // Repository에서 모든 퀴즈 가져오기
    }
    
    // **퀴즈 삭제 메서드**
    @Transactional
    public void deleteQuiz(int quizId) {
        List<QuizMusic> quizMusicList = quizMusicRepository.findByQuizId(quizId);
        if (quizMusicList.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND_QUIZ, "삭제할 퀴즈가 없습니다.");
        }
        quizMusicRepository.deleteAll(quizMusicList);
    }

 // **퀴즈 정답 제출 메서드**
    @Transactional
    public boolean submitAnswer(int memberId, int quizId, String userAnswer) {
        // 1. 퀴즈 목록 조회
    	List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);

        
        // 2. 사용자의 입력을 공백 제거 및 소문자로 변환
        String normalizedUserAnswer = userAnswer.replaceAll("\\s+", "").toLowerCase();

        // 3. 정답 비교 (퀴즈 정답도 공백 제거 및 소문자로 변환)
        boolean isCorrect = quizList.stream().anyMatch(quiz ->
                quiz.getAnswer().stream()
                        .anyMatch(answer -> 
                                answer.replaceAll("\\s+", "").equalsIgnoreCase(normalizedUserAnswer)
                        )
        );

        // 4. 멤버 정보 조회 및 게임 통계 업데이트
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_MEMBER));
        updateGameStats(member.getMemberGameStats(), isCorrect);

        // 5. 정답 여부에 따라 채팅 메시지 전송
        chatService.sendQuizMessage(quizId,
                isCorrect ? QuizMessage.MessageType.CORRECT : QuizMessage.MessageType.INCORRECT,
                isCorrect ? "정답입니다!" : "오답입니다!", member.getNickname());

        return isCorrect;
    }


    // **게임 통계 업데이트 메서드**
    private void updateGameStats(MemberGameStats stats, boolean isCorrect) {
        if (isCorrect) {
            stats.setWinNum(stats.getWinNum() + 1);
        } else {
            stats.setLoseNum(stats.getLoseNum() + 1);
        }
        stats.setTotalGameNum(stats.getTotalGameNum() + 1);
        stats.setPlayTime(stats.getPlayTime() + calculatePlayTime());
    }

    // **플레이 시간 계산 메서드**
    private int calculatePlayTime() {
        return 60;
    }

    // **참가자 닉네임 목록 가져오기**
    public List<String> getNicknameList(List<QuizRoomAttendee> attendees) {
        List<String> nicknameList = new ArrayList<>();
        attendees.forEach(attendee -> nicknameList.add(attendee.getMemberNickname()));
        return nicknameList;
    }

    // **힌트 생성 메서드**
    public String generateHint(int quizId) {
        List<QuizMusic> quizList = quizMusicRepository.findByQuizId(quizId);
        if (quizList.isEmpty()) {
            return "힌트를 제공할 수 없습니다.";
        }

        String answer = quizList.get(0).getAnswer().get(0);
        int hintLength = hintProgress.getOrDefault(quizId, 0) + 1;

        StringBuilder hintBuilder = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            if (i < hintLength) {
                hintBuilder.append(answer.charAt(i));
            } else {
                hintBuilder.append('○');
            }
        }

        hintProgress.put(quizId, hintLength);
        return hintBuilder.toString();
    }
}
