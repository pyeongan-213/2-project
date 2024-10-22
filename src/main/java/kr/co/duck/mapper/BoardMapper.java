package kr.co.duck.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import kr.co.duck.beans.ContentBean;
import kr.co.duck.beans.ReplyBean;

public interface BoardMapper {

	@Insert("insert into boardpost (boardpost_id, content_title, content_text, writedate, like_count, board_id, member_id) "
			+ "values (boardpost_seq.nextval, #{content_title}, #{content_text, jdbcType=CLOB}, sysdate, 0, #{board_id}, #{member_id})")
	void addContentInfo(ContentBean writeContentBean);

	@Insert("insert into reply (reply_id, reply_text, reply_date, boardpost_id, member_id) "
			+ "values (reply_seq.nextval, #{reply_text}, sysdate, #{boardpost_id}, #{member_id})")
	void addReply(ReplyBean writeReplyBean);

	@Select("select c.board_id, c.boardpost_id, c.content_title, c.content_text, m.nickname as membername, to_char(c.writedate, 'yyyy-mm-dd') as writedate, c.like_count "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "order by c.boardpost_id desc")
	List<ContentBean> getContentList(RowBounds rowBounds);

	@Select("select c.board_id, c.boardpost_id, c.content_title, c.content_text, m.nickname as membername, to_char(c.writedate, 'yyyy-mm-dd') as writedate, c.like_count "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "where c.like_count > 0 "
			+ "order by like_count desc, c.boardpost_id desc "
			+ "fetch first 5 rows only")
	List<ContentBean> getBestList();
	
	@Select("select c.board_id, c.boardpost_id, c.content_title, c.content_text, m.nickname as membername, to_char(c.writedate, 'yyyy-mm-dd') as writedate, c.like_count "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "where c.board_id = #{board_id} "
			+ "order by c.boardpost_id desc")
	List<ContentBean> getsortedList(int board_id, RowBounds rowBounds);
	
	@Select("select r.reply_id, r.member_id, m.nickname as reply_writer_name, to_char(r.reply_date, 'yyyy-mm-dd') as reply_date, r.reply_text "
			+ "from reply r "
			+ "join boardpost c on r.boardpost_id = c.boardpost_id "
			+ "join member m on r.member_id = m.member_id "
			+ "where c.boardpost_id=#{boardpost_id}")
	List<ReplyBean> getReplyList(int boardpost_id);

	@Select("select c.board_id, c.member_id, c.boardpost_id, c.content_title, m.nickname as membername, to_char(c.writedate, 'yyyy-mm-dd') as writedate, c.content_text, c.like_count "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "where c.boardpost_id=#{boardpost_id}")
	ContentBean getContentInfo(int boardpost_id);
	
	@Update("update boardpost "
			+ "set content_title = #{content_title}, "
			+ "content_text = #{content_text, jdbcType=CLOB}, "
			+ "writedate = sysdate "
			+ "where boardpost_id = #{boardpost_id}")
	void modifyContentInfo(ContentBean modifyContentBean);
	
	@Delete("delete from boardpost where boardpost_id = #{boardpost_id}")
	void deleteContent(int boardpost_id);

	@Delete("delete from reply where reply_id = #{reply_id}")
	void deleteReply(int reply_id);

	@Update("update boardpost "
			+ "set like_count = like_count + 1 "
			+ "where boardpost_id = #{boardpost_id}")
	int incrementLikeCount(int boardpost_id);
	
	@Update("update boardpost "
			+ "set like_count = like_count - 1 "
			+ "where boardpost_id = #{boardpost_id}")
	int decrementLikeCount(int boardpost_id);

	@Select("select like_count from boardpost where boardpost_id = #{boardpost_id}")
	Integer getLikeCount(int boardpost_id);

	@Select("select count(*) "
			+ "from boardpost ")
	int getContentCnt();

	@Select("select count(*) "
			+ "from boardpost "
			+ "where board_id = #{board_id}")
	int getSortedContentCnt(int board_id);

	@Select("select count(*) "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "WHERE (LOWER(c.content_title) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(c.content_text) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(m.nickname) LIKE LOWER('%' || #{query} || '%'))")
	int getAllSearchedContentCnt(@Param("query") String query);

	@Select("select c.board_id, c.boardpost_id, c.content_title, c.content_text, m.nickname as membername, to_char(c.writedate, 'yyyy-mm-dd') as writedate, c.like_count "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "WHERE (LOWER(c.content_title) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(c.content_text) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(m.nickname) LIKE LOWER('%' || #{query} || '%')) "
	        + "order by c.boardpost_id desc")
	List<ContentBean> searchAllPosts(@Param("query") String query, RowBounds rowBounds);

	@Select("select count(*) "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "WHERE (LOWER(c.content_title) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(c.content_text) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(m.nickname) LIKE LOWER('%' || #{query} || '%')) "
	        + "AND c.board_id = #{boardId}")
	int getSearchedContentCnt(@Param("boardId") int boardId, @Param("query") String query);

	@Select("select c.board_id, c.boardpost_id, c.content_title, c.content_text, m.nickname as membername, to_char(c.writedate, 'yyyy-mm-dd') as writedate, c.like_count "
			+ "from boardpost c "
			+ "join member m on c.member_id = m.member_id "
			+ "WHERE (LOWER(c.content_title) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(c.content_text) LIKE LOWER('%' || #{query} || '%') "
	        + "OR LOWER(m.nickname) LIKE LOWER('%' || #{query} || '%')) "
	        + "AND c.board_id = #{boardId} "
	        + "order by c.boardpost_id desc")
	List<ContentBean> searchPosts(@Param("boardId") int boardId, @Param("query") String query, RowBounds rowBounds);
}
