package kr.co.duck.beans;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberBean {
	private boolean memberNameExist;
	private boolean memberLogin;
	private int member_id; // 유저 식별번호

	@Size(min = 1, max = 10)
	@Pattern(regexp = "[a-zA-Z0-9]*")
	private String member_name; // 아이디

	@Size(min = 2, max = 5)
	@Pattern(regexp = "[가-힣a-zA-Z0-9]*")
	private String nickname; // 닉네임

	@Size(min = 5, max = 20)
	@Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\-`~]*")
	private String password; // 유저 비밀번호

	@Size(min = 5, max = 20)
	@Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\-`~]*")
	private String password2; // 유저 비밀번호 확인

	@Size(min = 1, max = 3)
	@Pattern(regexp = "[0-9]*")
	private String age; // 유저 나이

	@Email
	private String email; // 유저 이메일

	@Pattern(regexp = "[가-힣a-zA-Z]*")
	private String real_name; // 실명

	private String join_date;
	private String role;

	// 기본 생성자
	public MemberBean() {
		this.memberNameExist = false;
		this.memberLogin = false;
	}

	// 새로운 생성자 추가
	public MemberBean(int member_id, String nickname, String email) {
		this.member_id = member_id;
		this.nickname = nickname;
		this.email = email;
	}

	// Getter와 Setter
	public boolean isMemberNameExist() {
		return memberNameExist;
	}

	public void setMemberNameExist(boolean memberNameExist) {
		this.memberNameExist = memberNameExist;
	}

	public boolean isMemberLogin() {
		return memberLogin;
	}

	public void setMemberLogin(boolean member_login) {
		this.memberLogin = member_login;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMembername() {
		return member_name;
	}

	public void setMembername(String member_name) {
		this.member_name = member_name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password;
	}

	public void setPassword2(String password) {
		this.password = password;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJoindate() {
		return join_date;
	}

	public void setJoindate(String join_date) {
		this.join_date = join_date;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "MemberBean [member_id=" + member_id + ", member_name=" + member_name + ", nickname=" + nickname
				+ ", email=" + email + ", role=" + role + "]";
	}
}
