package Cocktail_Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import basic.Cocktail_TagVO;
import basic.JDBCConnection;

public class Cocktail_TagDAO {
	
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public int addTag_Cocktail(Cocktail_TagVO vo) {
		int count = -1;
		
		try {
			conn = JDBCConnection.getConnection();
			String sql = "INSERT INTO COCKTAIL_TAG VALUES(?, ?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, vo.getCocktailId());
			stmt.setInt(2, vo.getTagId());
			count = stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCConnection.close(stmt, conn);
		}	
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
