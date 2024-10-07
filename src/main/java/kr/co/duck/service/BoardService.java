package kr.co.duck.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import kr.co.duck.beans.ContentBean;
import kr.co.duck.beans.MemberBean;
import kr.co.duck.beans.PageBean;
import kr.co.duck.beans.ReplyBean;
import kr.co.duck.dao.BoardDao;

@Service
@PropertySource("/WEB-INF/properties/option.properties")
public class BoardService {

	@Autowired
	private BoardDao boardDao;

	@Resource(name = "loginMemberBean")
	private MemberBean loginMemberBean;

	@Value("${path.upload}")
	private String path_upload;

	@Value("${page.listcnt}")
	private int page_listcnt;

	@Value("${page.paginationcnt}")
	private int page_paginationcnt;

	public void addContent(ContentBean writeContentBean) {
		
		writeContentBean.setMember_id(loginMemberBean.getMember_id());
		boardDao.addContentInfo(writeContentBean);
	}

	public void addReply(ReplyBean writeReplyBean) {
		writeReplyBean.setMember_id(loginMemberBean.getMember_id());
		boardDao.addReply(writeReplyBean);
	}
	
	public List<ContentBean> getContentList(int page){
		
		int start = (page-1)*page_listcnt;
		
		RowBounds rowBounds = new RowBounds(start, page_listcnt);
		
		return boardDao.getContentList(rowBounds);
	}
	
	public List<ContentBean> getBestList(){
		return boardDao.getBestList();
	}

	public List<ContentBean> getsortedList(int board_id, int page){
		
		int start = (page-1)*page_listcnt;
		
		RowBounds rowBounds = new RowBounds(start, page_listcnt);
		
		return boardDao.getsortedList(board_id, rowBounds);
	}
	
	public ContentBean getContentInfo(int boardpost_id) {
		return boardDao.getContentInfo(boardpost_id);
	}

	public List<ReplyBean> getReplyList(int boardpost_id){
		return boardDao.getReplyList(boardpost_id);
	}
	
	public void modifyContentInfo(ContentBean modifyContentBean) {
		boardDao.modifyContentInfo(modifyContentBean);
	}
	public void deleteContent(int boardpost_id) {
		boardDao.deleteContent(boardpost_id);
	}
	
	public void deleteReply(int reply_id) {
		boardDao.deleteReply(reply_id);
	}
	
	public String getBoardInfoName(int board_info_idx) {
		return boardDao.getBoardInfoName(board_info_idx);
	}

	public int addLike(int boardpost_id) {
        return boardDao.incrementLikeCount(boardpost_id);
    }

    public int removeLike(int boardpost_id) {
        return boardDao.decrementLikeCount(boardpost_id);
    }
	
	public int getLikeCount(int boardpost_id) {
		Integer count = boardDao.getLikeCount(boardpost_id);
		return count != null ? count : 0;
	}

	public PageBean getContentCnt(int currentPage) {
		
		int contentCnt = boardDao.getContentCnt();
		PageBean pageBean = new PageBean(contentCnt, currentPage, page_listcnt, page_paginationcnt);
		
		return pageBean;
	}

	public PageBean getSortedContentCnt(int board_id, int currentPage) {
		
		int contentCnt = boardDao.getSortedContentCnt(board_id);
		PageBean pageBean = new PageBean(contentCnt, currentPage, page_listcnt, page_paginationcnt);
		
		return pageBean;
	}
}
