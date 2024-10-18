package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "MEMBER_SEQ", allocationSize = 1)
    @Column(name = "MEMBER_ID")
    private int memberId;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NICKNAME")
    private String nickname;

    // 기본 생성자 (JPA를 위해 필요)
    public Member() {
    }

    // 임시 객체 생성을 위한 생성자
    public Member(int memberId, String nickname, String email) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
    }

    // ID와 닉네임으로 생성하는 생성자
    public Member(int memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Member [memberId=" + memberId + ", email=" + email + ", password=" + password + ", nickname=" + nickname + "]";
    }
}
