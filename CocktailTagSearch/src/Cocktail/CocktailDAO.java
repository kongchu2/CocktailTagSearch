package Cocktail;

import java.sql.*;
import java.util.ArrayList;

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
			conn = JDBCConnection.getConenction();
			
			String sql = "SELECT * FROM COCKTAIL WHERE COCKTAIL_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, cocktail_id);
			
			rs = stmt.executeQuery();
				
			if(rs.next())
			{
				int id			= rs.getInt("COCKTAIL_ID");
				String name 	= rs.getString("NAME");
				String image 	= "image/" + rs.getString("IMAGE");
				String desc 	= rs.getString("DESC").replace("\\n", "<br>");
				String history 	= rs.getString("HISTORY_DESC").replace("\\n", "<br>");
				String taste 	= rs.getString("TASTE_DESC").replace("\\n", "<br>");
				String base		= rs.getString("BASE_ALCOHOL").replace("\\n", "<br>");
				String build 	= rs.getString("BUILD_METHOD").replace("\\n", "<br>");
				String glass	= rs.getString("COCKTAIL_GLASS").replace("\\n", "<br>");
				
				TagDAO dao = new TagDAO();
				ArrayList<TagVO> tagList =  dao.getTagListByCocktailId(id);
				
				cocktail = new CocktailVO();
				
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
			conn = JDBCConnection.getConenction();
			
			String sql = "SELECT * FROM COCKTAIL";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int id			= rs.getInt("COCKTAIL_ID");
				String name 	= rs.getString("NAME");
				String image 	= "image/" + rs.getString("IMAGE");
				String desc 	= rs.getString("DESC").replace("\\n", "<br>");
				String history 	= rs.getString("HISTORY_DESC").replace("\\n", "<br>");
				String taste 	= rs.getString("TASTE_DESC").replace("\\n", "<br>");
				String base		= rs.getString("BASE_ALCOHOL").replace("\\n", "<br>");
				String build 	= rs.getString("BUILD_METHOD").replace("\\n", "<br>");
				String glass	= rs.getString("COCKTAIL_GLASS").replace("\\n", "<br>");
				
				TagDAO dao = new TagDAO();
				ArrayList<TagVO> tagList =  dao.getTagListByCocktailId(id);
				
				CocktailVO cocktail = new CocktailVO();
				
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
				
				cocktailList.add(cocktail);
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
			conn = JDBCConnection.getConenction();
			
			String sql = "SELECT COCKTAIL_ID FROM COCKTAIL_TAG WHERE TAG_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, tagId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				int cocktail_id = rs.getInt("COCKTAIL_ID");
				sql = "SELECT * FROM COCKTAIL WHERE COCKTAIL_ID=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, cocktail_id);
				ResultSet cocktail_rs = stmt.executeQuery();
				if(cocktail_rs.next())
				{
					
					int id			= cocktail_rs.getInt("COCKTAIL_ID");
					String name 	= cocktail_rs.getString("NAME");
					String image 	= "image/" + cocktail_rs.getString("IMAGE");
					String desc 	= cocktail_rs.getString("DESC").replace("\\n", "<br>");
					String history 	= cocktail_rs.getString("HISTORY_DESC").replace("\\n", "<br>");
					String taste 	= cocktail_rs.getString("TASTE_DESC").replace("\\n", "<br>");
					String base		= cocktail_rs.getString("BASE_ALCOHOL").replace("\\n", "<br>");
					String build 	= cocktail_rs.getString("BUILD_METHOD").replace("\\n", "<br>");
					String glass	= cocktail_rs.getString("COCKTAIL_GLASS").replace("\\n", "<br>");
					
					TagDAO dao = new TagDAO();
					ArrayList<TagVO> tagList =  dao.getTagListByCocktailId(id);
					
					CocktailVO cocktail = new CocktailVO();
					
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
					
					cocktailList.add(cocktail);
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
			conn = JDBCConnection.getConenction();
			
			String sql = "SELECT max(COCKTAIL_ID) as max FROM COCKTAIL_TAG";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int max = 0;
			if(rs.next()) {
				max = rs.getInt("max");
			}
			ArrayList<Integer> cocktailIdList = new ArrayList<Integer>();
			for(int i=0;i<=max;i++) {
				sql = "SELECT TAG_ID FROM COCKTAIL_TAG WHERE COCKTAIL_ID=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, i);
				rs = stmt.executeQuery();
				ArrayList<Integer> temp_tagIdList = new ArrayList<Integer>();
				while(rs.next()) {
					temp_tagIdList.add(rs.getInt("TAG_ID"));
				}
				for(int tagid : temp_tagIdList) {
					boolean flag = true;
					if(!tagList.contains(tagid)) {
						flag = false;
					}
					if(flag) {
						cocktailIdList.add(i);
					}
				}
			}
			for(int cocktailId : cocktailIdList) {
				sql = "SELECT * FROM COCKTAIL WHERE COCKTAIL_ID=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, cocktailId);
				rs = stmt.executeQuery();
				while(rs.next())
				{
					int id			= rs.getInt("COCKTAIL_ID");
					String name 	= rs.getString("NAME");
					String image 	= "image/" + rs.getString("IMAGE");
					String desc 	= rs.getString("DESC").replace("\\n", "<br>");
					String history 	= rs.getString("HISTORY_DESC").replace("\\n", "<br>");
					String taste 	= rs.getString("TASTE_DESC").replace("\\n", "<br>");
					String base		= rs.getString("BASE_ALCOHOL").replace("\\n", "<br>");
					String build 	= rs.getString("BUILD_METHOD").replace("\\n", "<br>");
					String glass	= rs.getString("COCKTAIL_GLASS").replace("\\n", "<br>");
					
					TagDAO dao = new TagDAO();
					ArrayList<TagVO> temp_tagList =  dao.getTagListByCocktailId(id);
					
					CocktailVO cocktail = new CocktailVO();
					
					cocktail.setId(id);
					cocktail.setName(name);
					cocktail.setImage(image);
					cocktail.setDesc(desc);
					cocktail.setHistory(history);
					cocktail.setTaste(taste);
					cocktail.setBase(base);
					cocktail.setBuild(build);
					cocktail.setGlass(glass);
					cocktail.setTagList(temp_tagList);
					
					cocktailList.add(cocktail);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return cocktailList;
	}
}
