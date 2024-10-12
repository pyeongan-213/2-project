package kr.co.duck.mapper;

import org.apache.ibatis.annotations.Mapper;
import kr.co.duck.beans.MusicBean;

@Mapper
public interface MusicMapper {
    void insertMusic(MusicBean music);
}
