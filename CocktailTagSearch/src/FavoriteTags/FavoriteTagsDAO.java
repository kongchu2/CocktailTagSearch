package FavoriteTags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import FavoriteCocktail.FavoriteCocktailVO;
import basic.JDBCConnection;

public class FavoriteTagsDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public FavoriteTagsVO getFavoriteTag(int member_id) {

		FavoriteTagsVO favoriteTag = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_TAGS WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, member_id);
			
			rs = stmt.executeQuery();
				
			if(rs.next())
			{
				int tagId = rs.getInt("TAG_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteTag = new FavoriteTagsVO();
				
				favoriteTag.setMemberId(tagId);
				favoriteTag.setTagId(memberId);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteTag;
	}
	public ArrayList<FavoriteTagsVO> getFavoriteTagList() {
		
		ArrayList<FavoriteTagsVO> favoriteTagList = new ArrayList<FavoriteTagsVO>();
		FavoriteCocktailVO favoriteTag = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_TAGS";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int cocktailId = rs.getInt("TAG_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteTag = new FavoriteCocktailVO();
				
				favoriteTag.setMemberId(cocktailId);
				favoriteTag.setCocktailId(memberId);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteTagList;
	}
}
