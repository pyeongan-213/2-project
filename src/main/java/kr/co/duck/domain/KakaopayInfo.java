package kr.co.duck.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Entity
@Repository
@Table(name = "KAKAOPAY_INFO")
public class KakaopayInfo {

    @Id
    @Column(name = "TID")
    private String tid;

    @Column(name = "SID")
    private String sid;

    // 기본 생성자 (JPA에서 필요)
    public KakaopayInfo() {
    }

    // 전체 필드에 대한 생성자
    public KakaopayInfo(String tid, String sid) {
        this.tid = tid;
        this.sid = sid;
    }

    // Getter 메서드
    public String getTid() {
        return tid;
    }

    public String getSid() {
        return sid;
    }

    // Builder 패턴 구현
    public static Builder builder() {
        return new Builder();
    }

    // KakaopayInfo Builder 클래스
    public static class Builder {
        private String tid;
        private String sid;

        public Builder tid(String tid) {
            this.tid = tid;
            return this;
        }

        public Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public KakaopayInfo build() {
            return new KakaopayInfo(tid, sid);
        }
    }

    // 'of' 메소드 구현 (정적 팩토리 메소드)
    public static KakaopayInfo of(String tid, String sid) {
        return new KakaopayInfo(tid, sid);
    }
}
