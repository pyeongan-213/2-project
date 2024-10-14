package kr.co.duck.util;

import kr.co.duck.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// 기능 : 유저 디테일 구현체
public class UserDetailsImpl implements UserDetails {
	private final Member member;
	private final String nickname;

	public UserDetailsImpl(Member member, String nickname) {
		this.member = member;
		this.nickname = nickname;
	}

	public Member getMember() {
		return member;
	}

	public String getNickname() {
		return nickname;
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
