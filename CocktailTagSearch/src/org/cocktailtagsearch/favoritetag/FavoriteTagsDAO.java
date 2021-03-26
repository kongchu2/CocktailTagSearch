package org.cocktailtagsearch.favoritetag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.cocktailtagsearch.db.connect.JDBCConnection;

public class FavoriteTagsDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public boolean switchlikeTag(int memberId, int tagId) {
		boolean like = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, tagId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID=?";
			} else {
				like = true;
				sql = "INSERT INTO FAVORITE_TAGS VALUES(?, ?)";
			}
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, tagId);
			stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCConnection.close(stmt, conn);
		}
		return like;
	}
	public ArrayList<FavoriteTagsVO> getFavoriteTagList() {
		
		ArrayList<FavoriteTagsVO> favoriteTagList = new ArrayList<FavoriteTagsVO>();
		FavoriteTagsVO favoriteTag = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_TAGS";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int tagId = rs.getInt("TAG_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteTag = new FavoriteTagsVO();

				favoriteTag.setMember_id(memberId);
				favoriteTag.setTag_id(tagId);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteTagList;
	}
	public ArrayList<Integer> getFavoriteTagIdListByMember_id(int member_id) {
		ArrayList<Integer> tagIdList = new ArrayList<Integer>();
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT TAG_ID FROM FAVORITE_TAGS WHERE MEMBER_ID="+member_id;
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int tagId = rs.getInt("TAG_ID");
				
				tagIdList.add(tagId);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return tagIdList;
	}
	public ArrayList<FavoriteTagsVO> getFavoriteTagListByMember_id(int member_id) {

		ArrayList<FavoriteTagsVO> favoriteTagList = new ArrayList<FavoriteTagsVO>();
		FavoriteTagsVO favoriteTag = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_TAGS WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, member_id);
			
			rs = stmt.executeQuery();
				
			while(rs.next())
			{
				int tagId = rs.getInt("TAG_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteTag = new FavoriteTagsVO();
				
				favoriteTag.setMember_id(memberId);
				favoriteTag.setTag_id(tagId);
				
				favoriteTagList.add(favoriteTag);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteTagList;
	}
	public int deleteFavoriteTagByMember_id(int member_id) {
		int deleteCount = -1;
		
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, member_id);
			deleteCount = stmt.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCConnection.close(stmt, conn);
		}
		
		return deleteCount;
	}
	public int deleteFavoriteTagsByMember_idAndTag_idList(int member_id, ArrayList<Integer> tag_idList) {
		int deleteCount = -1;
		
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID IN(!)";
			
			String subQueryWhere = "";
			for(int tagId : tag_idList) {
				subQueryWhere += tagId + ", ";
			}
			subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length()-2);
			sql = sql.replace("!", subQueryWhere);
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, member_id);
			deleteCount = stmt.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCConnection.close(stmt, conn);
		}
		
		return deleteCount;
	}
}
