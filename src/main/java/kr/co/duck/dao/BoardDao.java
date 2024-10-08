package kr.co.duck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.duck.beans.ContentBean;
import kr.co.duck.beans.ReplyBean;
import kr.co.duck.mapper.BoardMapper;

@Repository
public class BoardDao {

	@Autowired
	private BoardMapper boardMapper;

	public void addContentInfo(ContentBean writeContentBean) {
		boardMapper.addContentInfo(writeContentBean);
	}

	public void addReply(ReplyBean writeReplyBean) {
		boardMapper.addReply(writeReplyBean);
	}
	
	public List<ContentBean> getContentList(RowBounds rowBounds){
		return boardMapper.getContentList(rowBounds);
	}

	public List<ContentBean> getBestList(){
		return boardMapper.getBestList();
	}

	public List<ContentBean> getsortedList(int board_id, RowBounds rowBounds){
		return boardMapper.getsortedList(board_id, rowBounds);
	}

	public ContentBean getContentInfo(int boardpost_id) {
		return boardMapper.getContentInfo(boardpost_id);
	}

	public List<ReplyBean> getReplyList(int boardpost_id){
		return boardMapper.getReplyList(boardpost_id);
	}

	public void modifyContentInfo(ContentBean modifyContentBean) {
		boardMapper.modifyContentInfo(modifyContentBean);
	}
	
	public void deleteContent(int boardpost_id) {
		boardMapper.deleteContent(boardpost_id);
	}

	public void deleteReply(int reply_id) {
		boardMapper.deleteReply(reply_id);
	}

	public String getBoardInfoName(int board_info_idx) {
		return boardMapper.getBoardInfoName(board_info_idx);
	}

	public int incrementLikeCount(int boardpost_id) {
        return boardMapper.incrementLikeCount(boardpost_id);
    }

    public int decrementLikeCount(int boardpost_id) {
        return boardMapper.decrementLikeCount(boardpost_id);
    }

    public int getLikeCount(int boardpost_id) {
        return boardMapper.getLikeCount(boardpost_id);
    }

    public int getContentCnt() {
		return boardMapper.getContentCnt();
	}

    public int getSortedContentCnt(int board_id) {
		return boardMapper.getSortedContentCnt(board_id);
	}

    public int getAllSearchedContentCnt(@Param("query")String query) {
		return boardMapper.getAllSearchedContentCnt(query);
	}

    public List<ContentBean> searchAllPosts(@Param("query") String query, RowBounds rowBounds) {
        return boardMapper.searchAllPosts(query, rowBounds);
    }

    public int getSearchedContentCnt(@Param("boardId")int boardId, @Param("query")String query) {
		return boardMapper.getSearchedContentCnt(boardId, query);
	}

    public List<ContentBean> searchPosts(@Param("boardId") int boardId, @Param("query") String query, RowBounds rowBounds) {
        return boardMapper.searchPosts(boardId, query, rowBounds);
    }
}
