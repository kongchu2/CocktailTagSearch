package org.cocktailtagsearch.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DAO {
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;

	public void close() {
		try {
			JDBCConnection.close(rs, stmt, conn);
		} catch (Exception e) {
			error(e);
		}
	}
	public void close_notIncludeRS() {
		try {
			JDBCConnection.close(stmt, conn);
		} catch (Exception e) {
			error(e);
		}
	}

	public int executeUpdateSQL(String sql, Object... list) {
		int count = -1;
		try {
			conn = JDBCConnection.getConnection();
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < list.length; i++)
				stmt.setObject(i+1, list[i]);
			count = stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				error(e1);
			}
			error(e);
		} finally {
			close_notIncludeRS();
		}
		return count;
	}

	public ArrayList<HashMap<String, Object>> executeSQL(String sql, Object... list) {
		ArrayList<HashMap<String,Object>> resultList = null;
		try {
			conn = JDBCConnection.getConnection();
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < list.length; i++)
				stmt.setObject(i+1, list[i]);
			rs = stmt.executeQuery();
			resultList = ResultSetParser.convertResultSetToArrayList(rs);
		} catch (Exception e) {
			error(e);
		} finally {
			close();
		}
		return resultList;
	}
	public void error(Exception e) {
		e.printStackTrace();
	}
}