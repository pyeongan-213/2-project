package kr.co.duck.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.dao.MemberDao;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;
	
	@Resource(name = "loginMemberBean")
	private MemberBean loginMemberBean;
	
	public boolean checkUserIdExist(String member_id) {
		String member_name = memberDao.checkMemberIdExist(member_id);
		
		if(member_name == null) {
			return true; //=아이디 사용할 수 있음
		}else {
			return false; //=아이디 사용할 수 없음
		}
	}
	
	public void addMemberInfo(MemberBean joinMemberBean) {
		memberDao.addMemberInfo(joinMemberBean);
	}
	
}
