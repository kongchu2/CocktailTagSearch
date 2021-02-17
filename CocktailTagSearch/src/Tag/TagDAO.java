package Tag;


import java.sql.*;
import java.util.ArrayList;

import basic.JDBCConnection;

public class TagDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	public TagVO getTag(int id) {
		
		TagVO tag = null;
		
		try {			
			conn = JDBCConnection.getConenction(); 
			
			String sql = "SELECT * FROM TAG WHERE TAG_ID="+id;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				tag = new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"), rs.getString("CATEGORY"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn); 
		}
		
		return tag;
	}
	public ArrayList<TagVO> getTagList() {
		
		ArrayList<TagVO> tagList = new ArrayList<TagVO>();
		
		try {
			conn = JDBCConnection.getConenction();
			
			stmt = conn.createStatement();
			String sql = "SELECT * FROM TAG";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"), rs.getString("CATEGORY")));
			}
		} catch(Exception e) {
			e.printStackTrace();		
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		
		return tagList;
		
	}
	public ArrayList<TagVO> getTagListByCocktailId(int cocktailId) {
		
		ArrayList<TagVO> tagList = null;
		
		try {
			conn = JDBCConnection.getConenction();
			
			String sql = "SELECT TAG_ID FROM COCKTAIL_TAG WHERE COCKTAIL_ID="+cocktailId;
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			tagList = new ArrayList<TagVO>();
			while(rs.next())
			{
				int tag_id = rs.getInt("TAG_ID");
				
				sql = "SELECT * FROM TAG WHERE TAG_ID="+tag_id;
				Statement statement = conn.createStatement();
				ResultSet tag_rs = statement.executeQuery(sql);
				
				if(tag_rs.next()) {
						 	tag_id 			= tag_rs.getInt("TAG_ID");
					String 	tag_name		= tag_rs.getString("TAG_NAME");
					String 	tag_desc		= tag_rs.getString("DESC");
					String 	tag_category 	= tag_rs.getString("CATEGORY");
					tagList.add(new TagVO(tag_id, tag_name, tag_desc, tag_category));
				}
				statement.close();
				tag_rs.close();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn); 
		}
		return tagList;
	}
}
