package kr.co.duck.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import kr.co.duck.beans.MemberBean;

public interface MemberMapper {

	@Select("select username from member where member_id = #{member_id}")
	String checkMemberIdExist(String member_id);
	
	@Insert("insert into member(member_id, membername, password, age, email, song_quantity, board_quantity, "
			+ "join_date, provider, provider_id) values (#{member_id}, #{membername}, #{password}, #{age}, "
			+ "#{email}, #{song_quantity}, #{board_quantity}, #{join_date}, #{provider}, #{provider_id}")
	void addMemberInfo(MemberBean joinMemberBean);
	
}
