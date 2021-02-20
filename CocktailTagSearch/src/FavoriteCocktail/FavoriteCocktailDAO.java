package FavoriteCocktail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import basic.JDBCConnection;

public class FavoriteCocktailDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public FavoriteCocktailVO getFavoriteCocktail(int member_id) {

		FavoriteCocktailVO favoriteCocktail = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, member_id);
			
			rs = stmt.executeQuery();
				
			if(rs.next())
			{
				int cocktailId = rs.getInt("COCKTAIL_ID");
				int memberId = rs.getInt("MEMBER_ID");
				
				favoriteCocktail = new FavoriteCocktailVO();
				
				favoriteCocktail.setMemberId(cocktailId);
				favoriteCocktail.setCocktailId(memberId);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteCocktail;
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
				
				favoriteCocktail.setMemberId(cocktailId);
				favoriteCocktail.setCocktailId(memberId);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return favoriteCocktailList;
	}
}
