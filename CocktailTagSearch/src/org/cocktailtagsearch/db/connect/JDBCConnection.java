package org.cocktailtagsearch.db.connect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JDBCConnection {
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		DataSource ds = null;
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Oracle11g");
		} catch(Exception e) {
			e.printStackTrace();
		}
		Connection conn  =null;
		try {
			conn = ds.getConnection();
		} catch(Exception e) {
			System.out.println("!@#$!@#$");
			e.printStackTrace();
			System.out.println("!@#$!@#$");
		}
		System.out.println(conn);
		return conn;
	}
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close (Statement stmt, Connection conn) {
		try {
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
