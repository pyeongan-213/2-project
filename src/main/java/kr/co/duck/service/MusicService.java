package kr.co.duck.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.duck.domain.Music;
import kr.co.duck.repository.MusicRepository;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    public List<Music> getAllMusic() {
        // null 체크 없이 바로 반환
        return musicRepository.findAll();
    }
}
