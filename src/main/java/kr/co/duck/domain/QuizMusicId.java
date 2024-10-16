package kr.co.duck.domain;

import java.io.Serializable;
import java.util.Objects;

public class QuizMusicId implements Serializable {

    private int quizId;
    private int musicId;

    // 기본 생성자
    public QuizMusicId() {}

    public QuizMusicId(int quizId, int musicId) {
        this.quizId = quizId;
        this.musicId = musicId;
    }

    // Getters and Setters
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    // equals와 hashCode 구현 (복합키 클래스 필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizMusicId that = (QuizMusicId) o;
        return quizId == that.quizId && musicId == that.musicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, musicId);
    }
}
