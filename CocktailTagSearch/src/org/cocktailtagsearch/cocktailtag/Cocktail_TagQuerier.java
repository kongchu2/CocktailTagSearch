package org.cocktailtagsearch.cocktailtag;

import java.util.ArrayList;

import org.cocktailtagsearch.db.access.DAO;

public class Cocktail_TagQuerier {
	
	private DAO dao = new DAO();
	
	public int addTag_Cocktail(Cocktail_TagVO vo) {
		int count = -1;
		String sql = "INSERT INTO COCKTAIL_TAG VALUES(?, ?)";
		count = dao.executeUpdateSQL(sql, vo.getTagId(),vo.getCocktailId());
		return count;
	}
	public int addTag_CocktailByList(ArrayList<Cocktail_TagVO> list) {
		int count = 0;
		for(Cocktail_TagVO vo : list) {
			count += addTag_Cocktail(vo);
		}
		return count;
	}
}
