package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBER_GAME_STATS")
public class MemberGameStats {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stats_seq")
    @SequenceGenerator(name = "stats_seq", sequenceName = "MEMBER_GAME_STATS_SEQ", allocationSize = 1)
    @Column(name = "STATS_ID")
    private int statsId;

    @Column(name = "MEMBER_ID", nullable = false)
    private int memberId;  // Member 객체 대신 int로 유지

    @Column(name = "WIN_NUM", nullable = false)
    private int winNum = 0;

    @Column(name = "LOSE_NUM", nullable = false)
    private int loseNum = 0;

    @Column(name = "TOTAL_GAME_NUM", nullable = false)
    private int totalGameNum = 0;

    @Column(name = "ENTER_GAME_NUM", nullable = false)
    private int enterGameNum = 0;

    @Column(name = "SOLO_EXIT_NUM", nullable = false)
    private int soloExitNum = 0;

    @Column(name = "MAKE_ROOM_NUM", nullable = false)
    private int makeRoomNum = 0;

    @Column(name = "PLAY_TIME", nullable = false)
    private int playTime = 0;
    
    @Column(name = "SCORE", nullable = false)
    private int score = 0;

    // 기본 생성자
    public MemberGameStats() {
    }

    // memberId로 초기화하는 생성자
    public MemberGameStats(int memberId) {
        this.memberId = memberId;
    }

    // Getter와 Setter
    public int getStatsId() {
        return statsId;
    }

    public void setStatsId(int statsId) {
        this.statsId = statsId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getWinNum() {
        return winNum;
    }

    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }

    public int getLoseNum() {
        return loseNum;
    }

    public void setLoseNum(int loseNum) {
        this.loseNum = loseNum;
    }

    public int getTotalGameNum() {
        return totalGameNum;
    }

    public void setTotalGameNum(int totalGameNum) {
        this.totalGameNum = totalGameNum;
    }

    public int getEnterGameNum() {
        return enterGameNum;
    }

    public void setEnterGameNum(int enterGameNum) {
        this.enterGameNum = enterGameNum;
    }

    public int getSoloExitNum() {
        return soloExitNum;
    }

    public void setSoloExitNum(int soloExitNum) {
        this.soloExitNum = soloExitNum;
    }

    public int getMakeRoomNum() {
        return makeRoomNum;
    }

    public void setMakeRoomNum(int makeRoomNum) {
        this.makeRoomNum = makeRoomNum;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	// 승리 횟수 업데이트 메서드
    public void incrementWinNum() {
        this.winNum += 1;
        this.totalGameNum += 1;
    }

    // 패배 횟수 업데이트 메서드
    public void incrementLoseNum() {
        this.loseNum += 1;
        this.totalGameNum += 1;
    }

    // 플레이 시간 포맷팅 메서드 (예: HH:mm:ss 포맷)
    public String getFormattedPlayTime() {
        int hours = playTime / 3600;
        int minutes = (playTime % 3600) / 60;
        int seconds = playTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return "MemberGameStats{" +
                "statsId=" + statsId +
                ", memberId=" + memberId +
                ", winNum=" + winNum +
                ", loseNum=" + loseNum +
                ", totalGameNum=" + totalGameNum +
                ", enterGameNum=" + enterGameNum +
                ", soloExitNum=" + soloExitNum +
                ", makeRoomNum=" + makeRoomNum +
                ", playTime=" + playTime +
                ", score=" + score +
                '}';
    }
}
