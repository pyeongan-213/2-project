package kr.co.duck.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import kr.co.duck.beans.MemberBean;

public interface MemberMapper {

	@Select("select nickname from member where membername = #{membername}")
	String checkMemberIdExist(String membername);
	
	@Insert("insert into member(member_id, membername, password, age, email, "
			+ "join_date, provider, provider_id) values (member_seq.nextval, #{membername}, #{password}, #{age}, "
			+ "#{email}, #{join_date}, #{provider}, #{provider_id}")
	void addMemberInfo(MemberBean joinMemberBean);
	
	@Select("select membername, nickname from member where membername = #{membername} and password = #{password}")
	MemberBean getLoginMemberInfo(MemberBean tempLoginMemberBean);
	
}
