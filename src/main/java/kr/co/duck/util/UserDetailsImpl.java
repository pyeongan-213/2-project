package kr.co.duck.util;

import kr.co.duck.domain.Member;
import kr.co.duck.beans.MemberBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L; // serialVersionUID 추가

	private final Member member;

	// MemberBean을 받는 생성자 추가
	public UserDetailsImpl(MemberBean memberBean) {
		this.member = new Member();
		this.member.setMemberId(memberBean.getMember_id());
		this.member.setNickname(memberBean.getNickname());
		this.member.setPassword(memberBean.getPassword());
		this.member.setEmail(memberBean.getEmail());
	}

	// 기존 Member를 받는 생성자 유지
	public UserDetailsImpl(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	public String getNickname() {
		return member.getNickname(); // Member에서 닉네임 가져오기
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null; // 권한 정보를 반환해야 할 경우 여기에 구현
	}

	@Override
	public String getPassword() {
		return member.getPassword(); // Member에서 비밀번호 가져오기
	}

	@Override
	public String getUsername() {
		return member.getEmail(); // Member에서 이메일이나 사용자 이름 가져오기
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 유저 계정 만료 여부
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 유저 계정 잠금 여부
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 자격 증명 만료 여부
	}

	@Override
	public boolean isEnabled() {
		return true; // 유저 계정 활성화 여부
	}
}
