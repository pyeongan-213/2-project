package kr.co.duck.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.domain.QuizMusic;
import kr.co.duck.repository.QuizMusicRepository;

@Service
public class QuizMusicService {

    // QuizMusicRepository 의존성 주입
    private final QuizMusicRepository quizmusicRepository;

    @Autowired
    public QuizMusicService(QuizMusicRepository quizmusicRepository) {
        this.quizmusicRepository = quizmusicRepository;
    }

  
    //모든 QuizMusic 데이터를 조회하여 반환하는 메서드.
    public List<QuizMusic> getAllMusic() {
        // 저장된 모든 QuizMusic 데이터를 반환
        return quizmusicRepository.findAll();
    }

   
 // musicId로 특정 QuizMusic 데이터를 조회하는 메서드.
    public QuizMusic getMusicById(int musicId) {
        // musicId로 QuizMusic 데이터 조회
        List<QuizMusic> musicList = quizmusicRepository.findByMusicId(musicId);

        if (musicList.isEmpty()) {
            throw new IllegalArgumentException("음악 ID " + musicId + "에 해당하는 데이터가 없습니다.");
        }

        // 첫 번째 결과 반환 (리스트의 첫 번째 항목 사용)
        return musicList.get(0);
    }

}
