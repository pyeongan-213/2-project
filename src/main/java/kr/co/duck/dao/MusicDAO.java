package kr.co.duck.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import kr.co.duck.beans.MusicBean;
import kr.co.duck.mapper.MusicMapper;

@Repository
public class MusicDAO {

    @Autowired
    private MusicMapper musicMapper;

    // MusicBean 정보를 DB에 저장하는 메서드
    public int saveMusic(MusicBean musicBean) {
        musicMapper.insertMusic(musicBean);
        return musicBean.getMusicID(); // MyBatis에서 자동으로 생성된 ID 값을 얻을 수 있습니다.
    }

    // MusicBean 정보를 ID로 조회하는 메서드
    public MusicBean getMusicById(int musicId) {
        return musicMapper.getMusicById(musicId);
    }
}
