package kr.co.duck.beans;

import javax.persistence.*;

@Entity
@Table(name = "KAKAOPAY_INFO")
public class KakaopayInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kakaopay_info_seq_gen")
    @SequenceGenerator(name = "kakaopay_info_seq_gen", sequenceName = "KAKAOPAY_INFO_SEQ", allocationSize = 1)
    @Column(name = "KAKAOPAY_INFO_ID")
    private int kakaopayInfoId;

    @Column(name = "MEMBER_ID")
    private int member_id;

    @Column(name = "TID", nullable = false)
    private String tid;

    @Column(name = "SID")
    private String sid;

    public KakaopayInfoBean() {
		// TODO Auto-generated constructor stub
	}

	public int getKakaopayInfoId() {
		return kakaopayInfoId;
	}

	public void setKakaopayInfoId(int kakaopayInfoId) {
		this.kakaopayInfoId = kakaopayInfoId;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
    
    
}
