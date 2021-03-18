package Cocktail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Cocktail_Tag.Cocktail_TagDAO;
import Tag.TagDAO;
import Tag.TagVO;
import basic.Cocktail_TagVO;
import basic.DAO;
import basic.JDBCConnection;
import basic.MapParser;
import basic.ResultSetParser;

public class CocktailDAO {
	private final int SCROLLING_LOAD_COUNT = 10;
	private DAO dao = new DAO();

	public CocktailVO getCocktail(int cocktail_id) {
		String sql = "SELECT * FROM COCKTAIL WHERE COCKTAIL_ID=?";
		HashMap<String, Object> map = dao.executeSQL(sql, cocktail_id).get(0);
		CocktailVO cocktail = MapParser.convertHashMaptoCocktailVO(map);
		TagDAO tagDao = new TagDAO();
		cocktail.setTagList(tagDao.getTagListByCocktailId(cocktail_id));
		return cocktail;
	}

	public ArrayList<CocktailVO> getCocktailList(int cocktailLength) {
		String sql = "SELECT rnum, cocktail.* FROM (SELECT ROWNUM rnum, COCKTAIL_ID, NAME, IMAGE, \"DESC\" "
				+ "FROM COCKTAIL WHERE ROWNUM <= ?) " + "cocktail WHERE rnum > ?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, cocktailLength + SCROLLING_LOAD_COUNT,cocktailLength);
		ArrayList<CocktailVO> cocktailList = MapParser.convertHashMapListtoCocktailList(list);
		TagDAO tagDao = new TagDAO();
		for (CocktailVO cocktail : cocktailList)
			cocktail.setTagList(tagDao.getTagListByCocktailId(cocktail.getId()));
		return cocktailList;
	}

	public ArrayList<CocktailVO> getSearchedCocktailList(String searchWord, int cocktailLength) {
		String sql = "SELECT * FROM (SELECT ROWNUM rnum, COCKTAIL_ID, NAME, IMAGE, \"DESC\" FROM COCKTAIL WHERE NAME LIKE'%"
				+ searchWord + "%' AND ROWNUM < ?) WHERE rnum > ?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, cocktailLength + SCROLLING_LOAD_COUNT,
				cocktailLength);
		ArrayList<CocktailVO> cocktailList = MapParser.convertHashMapListtoCocktailList(list);
		TagDAO tagDao = new TagDAO();
		for (CocktailVO cocktail : cocktailList)
			cocktail.setTagList(tagDao.getTagListByCocktailId(cocktail.getId()));
		return cocktailList;
	}

	@Deprecated
	private ArrayList<CocktailVO> getCocktailListByTag(int tagId) {
		return null;
	}

	public ArrayList<CocktailVO> getCocktailListByTagList(ArrayList<Integer> tagList, int cocktailLength) {
		String sql = "SELECT ROWNUM rnum, COCKTAIL_ID, NAME, IMAGE, \"DESC\" " + "FROM COCKTAIL "
				+ "WHERE COCKTAIL_ID IN " + "(SELECT COCKTAIL_ID " + "FROM (SELECT * FROM COCKTAIL_TAG WHERE !) "
				+ "GROUP BY COCKTAIL_ID " + "HAVING COUNT(*) > ?) " + "AND ROWNUM < ?";
		String subQueryWhere = "";
		for (int tagId : tagList) {
			subQueryWhere += " TAG_ID=\'" + tagId + "\' OR";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
		sql = sql.replace("!", subQueryWhere);
		sql = "SELECT rnum, cocktail.* FROM (" + sql;
		sql += ") cocktail WHERE rnum > ?";

		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, tagList.size() - 1,
				cocktailLength + SCROLLING_LOAD_COUNT, cocktailLength);
		ArrayList<CocktailVO> cocktailList = MapParser.convertHashMapListtoCocktailList(list);
		TagDAO tagDao = new TagDAO();
		for (CocktailVO cocktail : cocktailList)
			cocktail.setTagList(tagDao.getTagListByCocktailId(cocktail.getId()));
		return cocktailList;
	}

	public ArrayList<CocktailVO> getSearchedCocktailListByTagList(String searchWord, ArrayList<Integer> tagList,
			int cocktailLength) {
		String sql = "SELECT ROWNUM rnum, COCKTAIL_ID, NAME, IMAGE, \"DESC\" " + "FROM COCKTAIL "
				+ "WHERE COCKTAIL_ID IN " + "(SELECT COCKTAIL_ID " + "FROM (SELECT * FROM COCKTAIL_TAG WHERE !) "
				+ "GROUP BY COCKTAIL_ID " + "HAVING COUNT(*) > ?) AND NAME LIKE'%" + searchWord + "%' "
				+ "AND ROWNUM < ?";
		String subQueryWhere = "";
		for (int tagId : tagList) {
			subQueryWhere += " TAG_ID=\'" + tagId + "\' OR";
		}
		subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
		sql = sql.replace("!", subQueryWhere);
		sql = "SELECT ROWNUM, cocktail.* FROM (" + sql;
		sql += ") cocktail WHERE rnum > ?";

		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, tagList.size() - 1,
				cocktailLength + SCROLLING_LOAD_COUNT, cocktailLength);
		ArrayList<CocktailVO> cocktailList = MapParser.convertHashMapListtoCocktailList(list);
		TagDAO tagDao = new TagDAO();
		for (CocktailVO cocktail : cocktailList)
			cocktail.setTagList(tagDao.getTagListByCocktailId(cocktail.getId()));
		return cocktailList;
	}

	public int InsertCocktail(CocktailVO cocktail, ArrayList<Integer> tagIdList) {
		String sql = "INSERT INTO COCKTAIL VALUES ((select max(cocktail_id)+1 from cocktail), ?, ?, ?, ?, ?, ?, ?, ?)";
		int cnt = dao.executeUpdateSQL(sql, cocktail.getName(), cocktail.getImage(), cocktail.getDesc(),
				cocktail.getHistory(), cocktail.getTaste(), cocktail.getGlass());
		Cocktail_TagDAO dao = new Cocktail_TagDAO();
		ArrayList<Cocktail_TagVO> list = new ArrayList<Cocktail_TagVO>();
		for (int tagId : tagIdList) {
			Cocktail_TagVO vo = new Cocktail_TagVO();
			vo.setCocktailId(cocktail.getId());
			vo.setTagId(tagId);
			list.add(vo);
		}
		dao.addTag_CocktailByList(list);
		return cnt;
	}

	/*
	 * public int InsertCocktailList(ArrayList<CocktailVO> cocktailList) { int
	 * success = 0; try { conn = JDBCConnection.getConnection();
	 * 
	 * ArrayList<CocktailVO> cl = getCocktailList(); int maxId = 0; for(CocktailVO c
	 * : cl) { if(c.getId() > maxId) { maxId = c.getId(); } }
	 * 
	 * String sql = "INSERT INTO COCKTAIL" +
	 * "(\"COCKTAIL_ID\", \"NAME\", \"IMAGE\", \"DESC\", \"HISTORY_DESC\", \"TASTE_DESC\", \"BASE_ALCOHOL\", \"BUILD_METHOD\", \"COCKTAIL_GLASS\") "
	 * + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; stmt = conn.prepareStatement(sql);
	 * 
	 * Iterator<CocktailVO> it = cocktailList.iterator(); while(it.hasNext()){
	 * CocktailVO cocktail = it.next(); stmt.setInt(1, ++maxId); stmt.setString(2,
	 * cocktail.getName()); stmt.setString(3, cocktail.getImage());
	 * stmt.setString(4, cocktail.getDesc()); stmt.setString(5,
	 * cocktail.getHistory()); stmt.setString(6, cocktail.getTaste());
	 * stmt.setString(7, cocktail.getBase()); stmt.setString(8,
	 * cocktail.getBuild()); stmt.setString(9, cocktail.getGlass());
	 * stmt.addBatch(); } int[] numUpdates = stmt.executeBatch(); for (int i=0; i <
	 * numUpdates.length; i++) { if (numUpdates[i] == -2)
	 * System.out.println("Execution " + i + ": unknown number of rows updated");
	 * else System.out.println("Execution " + i + "successful: " + numUpdates[i] +
	 * " rows updated"); }
	 * 
	 * success = cocktailList.size();
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * JDBCConnection.close(rs, stmt, conn); } return success; }
	 */
	public boolean UpdateCocktail(CocktailVO cocktail, int cocktail_id) {
		int update = 0;
		int delete = 0;
		int insert = 0;

		String sql = "UPDATE COCKTAIL "
				+ "SET NAME=?, IMAGE=?, \"DESC\"=?, HISTORY_DESC=?, TASTE_DESC=?, BASE_ALCOHOL=?, BUILD_METHOD=?, COCKTAIL_GLASS=? WHERE COCKTAIL_ID = "
				+ cocktail_id;
		update = dao.executeUpdateSQL(sql, cocktail.getName(), cocktail.getImage().replace("image/cocktail/", ""),
				cocktail.getDesc(), cocktail.getHistory(), cocktail.getTaste(), cocktail.getGlass());

		sql = "DELETE FROM COCKTAIL_TAG WHERE COCKTAIL_ID=?";
		delete = dao.executeUpdateSQL(sql, cocktail_id);

		sql = "INSERT INTO COCKTAIL_TAG VALUES(?, ?)";
		for (TagVO tag : cocktail.getTagList()) {
			insert += dao.executeUpdateSQL(sql, tag.getId(), cocktail_id);
		}
		return update > 0;
	}

	public int DeleteCocktail(int cocktail_id) {
		int success = 0;
		String sql = "DELETE FROM COCKTAIL WHERE COCKTAIL_ID=?";
		success = dao.executeUpdateSQL(sql, cocktail_id);
		return success;
	}
}
