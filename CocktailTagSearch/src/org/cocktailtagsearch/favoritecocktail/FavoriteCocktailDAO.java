package org.cocktailtagsearch.favoritecocktail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.cocktailtagsearch.db.connect.JDBCConnection;

public class FavoriteCocktailDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public boolean switchlikePost(int memberId, int cocktailId) {
		boolean like = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, cocktailId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
			} else {
				like = true;
				sql = "INSERT INTO FAVORITE_COCKTAIL VALUES(?, ?)";
			}
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, cocktailId);
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
	public ArrayList<FavoriteCocktailVO> getFavoriteCocktailList() {
		
		ArrayList<FavoriteCocktailVO> favoriteCocktailList = new ArrayList<FavoriteCocktailVO>();
		FavoriteCocktailVO favoriteCocktail = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_COCKTAIL";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int cocktailId = rs.getInt("COCKTAIL_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteCocktail = new FavoriteCocktailVO();

				favoriteCocktail.setMember_id(memberId);
				favoriteCocktail.setCocktail_id(cocktailId);
				
				favoriteCocktailList.add(favoriteCocktail);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteCocktailList;
	}
	public ArrayList<FavoriteCocktailVO> getFavoriteCocktailListByMember_id(int member_id) {

		ArrayList<FavoriteCocktailVO> favoriteCocktailList = new ArrayList<FavoriteCocktailVO>();
		FavoriteCocktailVO favoriteCocktail = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, member_id);
			
			rs = stmt.executeQuery();
				
			while(rs.next())
			{
				int cocktailId = rs.getInt("COCKTAIL_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteCocktail = new FavoriteCocktailVO();

				favoriteCocktail.setMember_id(memberId);
				favoriteCocktail.setCocktail_id(cocktailId);
				
				favoriteCocktailList.add(favoriteCocktail);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteCocktailList;
	}
	public int deleteFavoriteCocktailByMember_id(int member_id) {
		int deleteCount = -1;
		
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=?";
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
	public int deleteFavoriteCocktailByMember_idAndCocktail_idList(int member_id, ArrayList<Integer> cocktail_idList) {
		int deleteCount = -1;
		
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID IN(!)";
			
			String subQueryWhere = "";
			for(int cocktailId : cocktail_idList) {
				subQueryWhere += cocktailId + ", ";
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
