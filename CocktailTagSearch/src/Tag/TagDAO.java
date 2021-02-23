package Tag;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

import Cocktail.CocktailVO;
import basic.JDBCConnection;

public class TagDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	public TagVO getTag(int id) {
		
		TagVO tag = null;
		
		try {			
			conn = JDBCConnection.getConnection(); 
			
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
			conn = JDBCConnection.getConnection();
			
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
			conn = JDBCConnection.getConnection();
			
			String sql = "SELECT * "
					+ "FROM TAG "
					+ "WHERE TAG_ID IN ("
						+ "SELECT TAG_ID "
						+ "FROM COCKTAIL_TAG "
						+ "WHERE COCKTAIL_ID=?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cocktailId);
			rs = pstmt.executeQuery();
			
			tagList = new ArrayList<TagVO>();
			while(rs.next())
			{
			 	int 	tag_id 			= rs.getInt("TAG_ID");
				String 	tag_name		= rs.getString("TAG_NAME");
				String 	tag_desc		= rs.getString("DESC");
				String 	tag_category 	= rs.getString("CATEGORY");
				tagList.add(new TagVO(tag_id, tag_name, tag_desc, tag_category));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn); 
		}
		return tagList;
	}
	public int InsertCocktail(TagVO tag) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			ArrayList<TagVO> tl = getTagList();
			int maxId = 0;
			for(TagVO t : tl) {
				if(t.getId() > maxId) {
					maxId = t.getId();
				}
			}
			
			String sql = "INSERT INTO TAG"
					   + "(\"TAG_ID\", \"TAG_NAME\", \"DESC\", \"CATEGORY\") " 
					   + "VALUES (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, ++maxId);
			pstmt.setString(2, tag.getName());
			pstmt.setString(3, tag.getDesc());
			pstmt.setString(4, tag.getCategory());
			
			success = pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return success;
	}
	public int UpdateCocktail(TagVO tag, int tag_id) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			TagVO before_tag = getTag(tag_id);
			
			String sql = "UPDATE TAG SET "
					   + "TAG_NAME=?, DESC=?, CATEGORY=? WHERE TAG_ID = "+tag_id;
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, tag.getName()       != null ? tag.getName()       : before_tag.getName());
			pstmt.setString(2, tag.getDesc()       != null ? tag.getDesc()       : before_tag.getDesc());
			pstmt.setString(3, tag.getCategory()   != null ? tag.getCategory()   : before_tag.getCategory());
			
			success = pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return success;
	}
	public int DeleteCocktail(int tag_id) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();
			
			String sql = "DELETE FROM COCKTAIL WHERE = "+tag_id;
			
			pstmt = conn.prepareStatement(sql);
			
			success = pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return success;
	}
}
