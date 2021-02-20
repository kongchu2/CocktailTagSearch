package FavoriteTags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import basic.JDBCConnection;

public class FavoriteTagsDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
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
}
