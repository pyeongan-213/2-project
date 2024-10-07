package kr.co.duck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.dao.MusicDAO;

@Service
public class MusicService {

    @Autowired
    private MusicDAO musicDAO;  // MusicDAO 인스턴스 주입

    private static int musicIdCounter = 1000; // 초기값 설정 (필요에 따라 조정)

    public void saveMusic(MusicBean musicBean) {
        // 고유한 숫자 ID 생성
        int musicId = generateMusicId();
        musicBean.setMusicId(musicId);

        // DB에 값 저장 (MusicDAO의 인스턴스 메서드 호출)
        musicDAO.saveMusic(musicBean);
    }

    // 고유한 숫자 ID 생성 메서드
    private synchronized int generateMusicId() {
        return musicIdCounter++;
    }
}
