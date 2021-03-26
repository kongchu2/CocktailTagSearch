package org.cocktailtagsearch.tag;

import java.util.ArrayList;
import java.util.HashMap;

import org.cocktailtagsearch.db.access.DAO;
import org.cocktailtagsearch.util.MapParser;

public class TagQuerier {
	private final int LIMIT = 5;
	DAO dao = new DAO();

	public TagVO getTag(int id) {
		String sql = "SELECT * FROM (SELECT * FROM TAG WHERE TAG_ID=?) WHERE  ROWNUM <= ?";
		HashMap<String, Object> map = dao.executeSQL(sql, id, LIMIT).get(0);
		TagVO tag = MapParser.convertHashMaptoTagVO(map);
		return tag;
	}

	public ArrayList<TagVO> getTagList() {
		String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE ROWNUM <= ? "
				+ " GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, LIMIT);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;
	}

	public ArrayList<TagVO> getTagListByTagIdList(ArrayList<Integer> tagIdList) {
		String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_ID IN(!) GROUP BY  TAG_ID, TAG_NAME, \"DESC\", CATEGORY ORDER BY TAG_ID";
		String subQueryWhere = "";
		if(tagIdList.size() < 1) {
			return new ArrayList<TagVO>();
		}
		for (int tagId : tagIdList) {
			subQueryWhere += tagId + ", ";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
		sql = sql.replace("!", subQueryWhere);
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;

	}

	public ArrayList<TagVO> getTagListByTagIdListWithoutTagIdList(ArrayList<Integer> tagIdList,
			ArrayList<Integer> withoutTagIdList) {
		String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_ID IN(1!) AND TAG_ID NOT IN(2!) GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";

		String subQueryWhere1 = "";
		for (int tagId : tagIdList) {
			subQueryWhere1 += tagId + ", ";
		}
		subQueryWhere1 = subQueryWhere1.substring(0, subQueryWhere1.length() - 2);
		sql = sql.replace("1!", subQueryWhere1);

		String subQueryWhere2 = "";
		for (int tagId : withoutTagIdList) {
			subQueryWhere2 += tagId + ", ";
		}
		subQueryWhere2 = subQueryWhere2.substring(0, subQueryWhere2.length() - 2);
		sql = sql.replace("2!", subQueryWhere2);
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;

	}

	public TagVO getSearchedTagJustOne(String searchWord) {
		String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_NAME LIKE'%" + searchWord
				+ "%' GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";
		HashMap<String, Object> map = dao.executeSQL(sql).get(0);
		TagVO tag = MapParser.convertHashMaptoTagVO(map);
		return tag;
	}

	public ArrayList<TagVO> getSearchedTagList(String searchWord) {
		String sql = "SELECT ROWNUM, TAG.* FROM (SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_NAME LIKE'%"
				+ searchWord + "%' GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY) TAG WHERE ROWNUM <= ?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, LIMIT);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;
	}

	public ArrayList<TagVO> getSearchedTagListWithoutTagIdList(String searchWord, ArrayList<Integer> tagIdList) {
		String sql = "SELECT ROWNUM, TAG.* FROM (SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_NAME LIKE'%"
				+ searchWord
				+ "%' AND TAG_ID NOT IN(!) GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY) TAG WHERE ROWNUM <= " + LIMIT;

		String subQueryWhere = "";
		for (int tagId : tagIdList) {
			subQueryWhere += tagId + ", ";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
		sql = sql.replace("!", subQueryWhere);

		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;
	}

	public ArrayList<TagVO> getTagListWithoutTagIdList(ArrayList<Integer> tagIdList) {
		String sql = "SELECT ROWNUM, TAG.* FROM (SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_ID NOT IN(!) GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY) TAG WHERE ROWNUM <= ?";

		String subQueryWhere = "";
		for (int tagId : tagIdList) {
			subQueryWhere += tagId + ", ";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
		sql = sql.replace("!", subQueryWhere);

		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, LIMIT);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);

		return tagList;
	}

	public ArrayList<TagVO> getTagListByCocktailId(int cocktailId) {
		String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY " + "FROM TAG " + "WHERE TAG_ID IN ("
				+ "SELECT TAG_ID " + "FROM COCKTAIL_TAG " + "WHERE COCKTAIL_ID=?) "
				+ "GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, cocktailId);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;
	}

	public int InsertTag(TagVO tag) {
		int success = 0;
		String sql = "INSERT INTO TAG" + "(\"TAG_ID\", \"TAG_NAME\", \"DESC\", \"CATEGORY\") "
				+ "VALUES ((select max(tag_id) from tag), ?, ?, ?)";
		success = dao.executeUpdateSQL(sql, tag.getName(), tag.getDesc(), tag.getCategory());

		return success;
	}

	public int UpdateCocktail(TagVO tag, int tag_id) {
		int success = 0;
		TagVO before_tag = getTag(tag_id);
		String sql = "UPDATE TAG SET " + "TAG_NAME=?, DESC=?, CATEGORY=? WHERE TAG_ID = " + tag_id;
		success = dao.executeUpdateSQL(sql, tag.getName() != null ? tag.getName() : before_tag.getName(),
				tag.getDesc() != null ? tag.getDesc() : before_tag.getDesc(),
				tag.getCategory() != null ? tag.getCategory() : before_tag.getCategory());
		return success;
	}
}
