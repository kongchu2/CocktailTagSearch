package org.cocktailtagsearch.favoritecocktail;


import java.util.ArrayList;
import java.util.HashMap;

import org.cocktailtagsearch.db.access.DAO;
import org.cocktailtagsearch.util.MapParser;

public class FavoriteCocktailQuerier {
	private DAO dao = new DAO();
	
	public boolean switchlikePost(int memberId, int cocktailId) {
		boolean like = false;
		String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, memberId, cocktailId);
		if(list.size() > 0) {
			sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
		} else {
			like = true;
			sql = "INSERT INTO FAVORITE_COCKTAIL VALUES(?, ?)";
		}
		dao.executeUpdateSQL(sql, memberId, cocktailId);
		return like;
	}
	public ArrayList<FavoriteCocktailVO> getFavoriteCocktailList() {
		String sql = "SELECT * FROM FAVORITE_COCKTAIL";
		ArrayList<FavoriteCocktailVO> favoriteCocktailList = MapParser.toFavoriteCocktailList(dao.executeSQL(sql));
		return favoriteCocktailList;
	}
	public ArrayList<FavoriteCocktailVO> getFavoriteCocktailListByMember_id(int member_id) {
		String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=?";
		ArrayList<FavoriteCocktailVO> favoriteCocktailList = MapParser.toFavoriteCocktailList(dao.executeSQL(sql, member_id));
		return favoriteCocktailList;
	}
	public int deleteFavoriteCocktailByMember_id(int member_id) {
		int deleteCount = -1;
		String sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=?";
		deleteCount = dao.executeUpdateSQL(sql, member_id);
		return deleteCount;
	}
	public int deleteFavoriteCocktailByMember_idAndCocktail_idList(int member_id, ArrayList<Integer> cocktail_idList) {
		int deleteCount = -1;
		String sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID IN(!)";
		String subQueryWhere = "";
		for(int cocktailId : cocktail_idList) {
			subQueryWhere += cocktailId + ", ";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length()-2);
		sql = sql.replace("!", subQueryWhere);
		deleteCount = dao.executeUpdateSQL(sql, member_id);
		return deleteCount;
	}
	
}
