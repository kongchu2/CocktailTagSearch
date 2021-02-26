package Cocktail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import Tag.TagDAO;
import Tag.TagVO;
import basic.JDBCConnection;

public class CocktailDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public CocktailVO getCocktail(int cocktail_id) {

		CocktailVO cocktail = null;
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * FROM COCKTAIL WHERE COCKTAIL_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, cocktail_id);
			
			rs = stmt.executeQuery();
				
			if(rs.next())
			{
				cocktail = getCocktailByResultSet();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktail;
	}
	public ArrayList<CocktailVO> getCocktailList() {
		
		ArrayList<CocktailVO> cocktailList = new ArrayList<CocktailVO>();
		
		try {
			conn = JDBCConnection.getConnection();

			// limit
			int limit = 10;
			String sql = "SELECT ROWNUM, cocktail.* FROM (SELECT COCKTAIL_ID, NAME, IMAGE, \"DESC\" FROM COCKTAIL) cocktail WHERE ROWNUM <= "+limit;
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{	
				cocktailList.add(getCocktailByResultSetNeedToSearch());
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktailList;
	}
	public ArrayList<CocktailVO> getSearchedCocktailList(String searchWord) {

		ArrayList<CocktailVO> cocktailList = new ArrayList<CocktailVO>();
		
		try {
			conn = JDBCConnection.getConnection();

			// limit
			int limit = 10;
			String sql = "SELECT ROWNUM, cocktail.* FROM (SELECT COCKTAIL_ID, NAME, IMAGE, \"DESC\" FROM COCKTAIL WHERE NAME LIKE'%"+ searchWord +"%') cocktail WHERE ROWNUM <= "+limit;
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{	
				cocktailList.add(getCocktailByResultSetNeedToSearch());
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktailList;
	}
	public ArrayList<CocktailVO> getCocktailListByTag(int tagId) {
		
		ArrayList<CocktailVO> cocktailList = new ArrayList<CocktailVO>();
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT COCKTAIL_ID FROM COCKTAIL_TAG WHERE TAG_ID=? ";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, tagId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				int cocktail_id = rs.getInt("COCKTAIL_ID");
				// limit
				int limit = 5;
				sql = "SELECT ROWNUM, cocktail.* FROM (SELECT COCKTAIL_ID, NAME, IMAGE, \"DESC\" FROM (SELECT COCKTAIL_ID, NAME, IMAGE, DESC FROM COCKTAIL WHERE COCKTAIL_ID=?)) cocktail WHERE ROWNUM <= "+limit;
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, cocktail_id);
				ResultSet cocktail_rs = stmt.executeQuery();
				if(cocktail_rs.next())
				{
					cocktailList.add(getCocktailByResultSetNeedToSearch());
				}
				cocktail_rs.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktailList;
	}
	public ArrayList<CocktailVO> getCocktailListByTagList(ArrayList<Integer> tagList) {
		ArrayList<CocktailVO> cocktailList = new ArrayList<CocktailVO>();
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT COCKTAIL_ID, NAME, IMAGE, \"DESC\" "
					+ "FROM COCKTAIL "
					+ "WHERE COCKTAIL_ID IN "
					+ "(SELECT COCKTAIL_ID "
					+ "FROM (SELECT * FROM COCKTAIL_TAG WHERE !) "
					+ "GROUP BY COCKTAIL_ID "
					+ "HAVING COUNT(*) > ?)";
			
			String subQueryWhere = "";
			
			for(int tagId : tagList) {
				subQueryWhere += " TAG_ID=\'"+tagId+"\' OR";
			}
			subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length()-2);
			sql = sql.replace("!", subQueryWhere);
			
			// limit
			int limit = 5;
			sql = "SELECT ROWNUM, cocktail.* FROM (" + sql;
			sql += ") cocktail WHERE ROWNUM <= " + limit;
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, tagList.size()-1);
			rs = stmt.executeQuery();
			while(rs.next()) {
				cocktailList.add(getCocktailByResultSetNeedToSearch());
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktailList;
	}
	public ArrayList<CocktailVO> getSearchedCocktailListByTagList(String searchWord, ArrayList<Integer> tagList) {
		ArrayList<CocktailVO> cocktailList = new ArrayList<CocktailVO>();
		
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT COCKTAIL_ID, NAME, IMAGE, \"DESC\" "
					+ "FROM COCKTAIL "
					+ "WHERE COCKTAIL_ID IN "
					+ "(SELECT COCKTAIL_ID "
					+ "FROM (SELECT * FROM COCKTAIL_TAG WHERE !) "
					+ "GROUP BY COCKTAIL_ID "
					+ "HAVING COUNT(*) > ?) AND NAME LIKE'%"+ searchWord +"%'";
			
			String subQueryWhere = "";
			
			for(int tagId : tagList) {
				subQueryWhere += " TAG_ID=\'"+tagId+"\' OR";
			}
			subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length()-2);
			sql = sql.replace("!", subQueryWhere);
			
			// limit
			int limit = 5;
			sql = "SELECT ROWNUM, cocktail.* FROM (" + sql;
			sql += ") cocktail WHERE ROWNUM <= " + limit;
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, tagList.size()-1);
			rs = stmt.executeQuery();
			while(rs.next()) {
				cocktailList.add(getCocktailByResultSetNeedToSearch());
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktailList;
	}
	
	public int InsertCocktail(CocktailVO cocktail) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			ArrayList<CocktailVO> cl = getCocktailList();
			int maxId = 0;
			for(CocktailVO c : cl) {
				if(c.getId() > maxId) {
					maxId = c.getId();
				}
			}
			
			String sql = "INSERT INTO COCKTAIL"
					   + "(\"COCKTAIL_ID\", \"NAME\", \"IMAGE\", \"DESC\", \"HISTORY_DESC\", \"TASTE_DESC\", \"BASE_ALCOHOL\", \"BUILD_METHOD\", \"COCKTAIL_GLASS\") " 
					   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, ++maxId);
			stmt.setString(2, cocktail.getName());
			stmt.setString(3, cocktail.getImage());
			stmt.setString(4, cocktail.getDesc());
			stmt.setString(5, cocktail.getHistory());
			stmt.setString(6, cocktail.getTaste());
			stmt.setString(7, cocktail.getBase());
			stmt.setString(8, cocktail.getBuild());
			stmt.setString(9, cocktail.getGlass());
			
			success = stmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return success;
	}
	/*
	public int InsertCocktailList(ArrayList<CocktailVO> cocktailList) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			ArrayList<CocktailVO> cl = getCocktailList();
			int maxId = 0;
			for(CocktailVO c : cl) {
				if(c.getId() > maxId) {
					maxId = c.getId();
				}
			}
			
			String sql = "INSERT INTO COCKTAIL"
					+ "(\"COCKTAIL_ID\", \"NAME\", \"IMAGE\", \"DESC\", \"HISTORY_DESC\", \"TASTE_DESC\", \"BASE_ALCOHOL\", \"BUILD_METHOD\", \"COCKTAIL_GLASS\") " + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);
			
			Iterator<CocktailVO> it = cocktailList.iterator();
			while(it.hasNext()){
				CocktailVO cocktail = it.next();
				stmt.setInt(1, ++maxId);
				stmt.setString(2, cocktail.getName());
				stmt.setString(3, cocktail.getImage());
				stmt.setString(4, cocktail.getDesc());
				stmt.setString(5, cocktail.getHistory());
				stmt.setString(6, cocktail.getTaste());
				stmt.setString(7, cocktail.getBase());
				stmt.setString(8, cocktail.getBuild());
				stmt.setString(9, cocktail.getGlass());
				stmt.addBatch();                      
			}  
			int[] numUpdates = stmt.executeBatch();
			  for (int i=0; i < numUpdates.length; i++) {
			    if (numUpdates[i] == -2)
			      System.out.println("Execution " + i + 
			        ": unknown number of rows updated");
			    else
			      System.out.println("Execution " + i + 
			        "successful: " + numUpdates[i] + " rows updated");
			}
			
			success = cocktailList.size();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return success;
	}
	*/
	public int UpdateCocktail(CocktailVO cocktail, int cocktail_id) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			CocktailVO before_cocktail = getCocktail(cocktail_id);
			
			String sql = "UPDATE COCKTAIL SET "
					   + "NAME=?, IMAGE=?, DESC=?, HISTORY_DESC=?, TASTE_DESC=?, BASE_ALCOHOL=?, BUILD_METHOD=?, GLCOCKTAIL_GLASSASS=? WHERE COCKTAIL_ID = "+cocktail_id;
			
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, cocktail.getName()    != null ? cocktail.getName()    : before_cocktail.getName());
			stmt.setString(2, cocktail.getImage()   != null ? cocktail.getImage()   : before_cocktail.getImage());
			stmt.setString(3, cocktail.getDesc()    != null ? cocktail.getDesc()    : before_cocktail.getDesc());
			stmt.setString(4, cocktail.getHistory() != null ? cocktail.getHistory() : before_cocktail.getHistory());
			stmt.setString(5, cocktail.getTaste()   != null ? cocktail.getTaste()   : before_cocktail.getTaste());
			stmt.setString(6, cocktail.getBase()    != null ? cocktail.getBase()    : before_cocktail.getBase());
			stmt.setString(7, cocktail.getBuild()   != null ? cocktail.getBuild()   : before_cocktail.getBuild());
			stmt.setString(8, cocktail.getGlass()   != null ? cocktail.getGlass()   : before_cocktail.getGlass());
			
			success = stmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return success;
	}
	public int DeleteCocktail(int cocktail_id) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "DELETE FROM COCKTAIL WHERE = "+cocktail_id;
			
			stmt = conn.prepareStatement(sql);
			
			success = stmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return success;
	}
	private CocktailVO getCocktailByResultSet() throws SQLException {
		CocktailVO cocktail = new CocktailVO();
		
		int id			= rs.getInt("COCKTAIL_ID");
		String name 	= rs.getString("NAME");
		String image 	= "image/cocktail/" + rs.getString("IMAGE");
		String desc 	= rs.getString("DESC");
		String history 	= rs.getString("HISTORY_DESC");
		String taste 	= rs.getString("TASTE_DESC");
		String base		= rs.getString("BASE_ALCOHOL");
		String build 	= rs.getString("BUILD_METHOD");
		String glass	= rs.getString("COCKTAIL_GLASS");
		
		TagDAO dao = new TagDAO();
		ArrayList<TagVO> tagList =  dao.getTagListByCocktailId(id);
		
		cocktail.setId(id);
		cocktail.setName(name);
		cocktail.setImage(image);
		cocktail.setDesc(desc);
		cocktail.setHistory(history);
		cocktail.setTaste(taste);
		cocktail.setBase(base);
		cocktail.setBuild(build);
		cocktail.setGlass(glass);
		cocktail.setTagList(tagList);
		
		return cocktail;
	}
	private CocktailVO getCocktailByResultSetNeedToSearch() throws SQLException {
		CocktailVO cocktail = new CocktailVO();
		
		int id			= rs.getInt("COCKTAIL_ID");
		String name 	= rs.getString("NAME");
		String image 	= "image/cocktail/" + rs.getString("IMAGE");
		String desc 	= rs.getString("DESC");
		
		TagDAO dao = new TagDAO();
		ArrayList<TagVO> tagList =  dao.getTagListByCocktailId(id);
		
		cocktail.setId(id);
		cocktail.setName(name);
		cocktail.setImage(image);
		cocktail.setDesc(desc);
		cocktail.setTagList(tagList);
		
		return cocktail;
	}
}
