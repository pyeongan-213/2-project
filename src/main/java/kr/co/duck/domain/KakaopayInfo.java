package kr.co.duck.domain;

import javax.persistence.*;

@Entity
@Table(name = "KAKAOPAY_INFO")
public class KakaopayInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kakaopay_info_seq")
    @SequenceGenerator(name = "kakaopay_info_seq", sequenceName = "KAKAOPAY_INFO_SEQ", allocationSize = 1)
    @Column(name = "KAKAOPAY_INFO_ID")
    private Long kakaopayInfoId;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "TID", nullable = false)
    private String tid;

    @Column(name = "SID")
    private String sid;

    // 기본 생성자 (JPA에서 필요)
    public KakaopayInfo() {
    }

    // 전체 필드에 대한 생성자
    public KakaopayInfo(String tid, String sid, Long memberId) {
        this.tid = tid;
        this.sid = sid;
        this.memberId = memberId;
    }

    // Getter 메서드
    public Long getKakaopayInfoId() {
        return kakaopayInfoId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getTid() {
        return tid;
    }

    public String getSid() {
        return sid;
    }

    // 'of' 메소드 구현 (정적 팩토리 메소드)
    public static KakaopayInfo of(String tid, String sid, Long memberId) {
        return new KakaopayInfo(tid, sid, memberId);
    }
}
