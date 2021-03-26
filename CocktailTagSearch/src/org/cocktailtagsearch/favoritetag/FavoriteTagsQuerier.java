package org.cocktailtagsearch.favoritetag;

import java.util.ArrayList;
import java.util.HashMap;

import org.cocktailtagsearch.db.access.DAO;
import org.cocktailtagsearch.util.Caster;
import org.cocktailtagsearch.util.MapParser;

public class FavoriteTagsQuerier {
	
	private DAO dao = new DAO();
	
	
	public boolean switchlikeTag(int memberId, int tagId) {
		boolean like = false;
		String sql = "SELECT * FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID=?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, memberId, tagId);
		
		if(list.size() > 0) {
			sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID=?";
		} else {
			like = true;
			sql = "INSERT INTO FAVORITE_TAGS VALUES(?, ?)";
		}
		dao.executeUpdateSQL(sql, memberId, tagId);
		return like;
	}
	public ArrayList<FavoriteTagsVO> getFavoriteTagList() {
		String sql = "SELECT * FROM FAVORITE_TAGS";
		ArrayList<FavoriteTagsVO> favoriteTagList = MapParser.toFavoriteTagList(dao.executeSQL(sql));
		return favoriteTagList;
	}
	public ArrayList<Integer> getFavoriteTagIdListByMember_id(int member_id) {
		ArrayList<Integer> tagIdList = new ArrayList<Integer>();
		String sql = "SELECT TAG_ID FROM FAVORITE_TAGS WHERE MEMBER_ID=?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, member_id);
		for(HashMap<String, Object> map : list) {
			tagIdList.add(Caster.bigDecimalObjToInt(map.get("TAG_ID")));
		}
		return tagIdList;
	}
	public ArrayList<FavoriteTagsVO> getFavoriteTagListByMember_id(int member_id) {
		String sql = "SELECT * FROM FAVORITE_TAGS WHERE MEMBER_ID=?";
		ArrayList<FavoriteTagsVO> favoriteTagList = MapParser.toFavoriteTagList(dao.executeSQL(sql, member_id));
		return favoriteTagList;
	}
	public int deleteFavoriteTagByMember_id(int member_id) {
		int deleteCount = -1;
		String sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=?";
		deleteCount = dao.executeUpdateSQL(sql, member_id);
		return deleteCount;
	}
	public int deleteFavoriteTagsByMember_idAndTag_idList(int member_id, ArrayList<Integer> tag_idList) {
		int deleteCount = -1;
		String sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID IN(!)";
		String subQueryWhere = "";
		for(int tagId : tag_idList) {
			subQueryWhere += tagId + ", ";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length()-2);
		sql = sql.replace("!", subQueryWhere);
		dao.executeUpdateSQL(sql, member_id);
		return deleteCount;
	}
}
