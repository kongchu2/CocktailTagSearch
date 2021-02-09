package Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basic.JDBCConnection;


public class MemberDAO {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	public MemberVO getMember(String login_id) {
		MemberVO member = null;
		try {
			conn = JDBCConnection.getConenction();
			String sql = "SELECT * FROM MEMBER WHERE LOGIN_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, login_id);
			rs = stmt.executeQuery();
			if(rs.next()) {
				member = new MemberVO();
				member.setMember_id(rs.getInt("MEMBER_ID"));
				member.setLogin_id(rs.getString("LOGIN_ID"));
				member.setName(rs.getString("NAME"));
				member.setPassword(rs.getString("PASSWORD"));
			} 
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return member;
	}
	public int addMember(MemberVO member) {
		int cnt = -1;
		try {
			conn = JDBCConnection.getConenction();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO MEMBER VALUES((SELECT nvl(max(MEMBER_ID), 0)+1 FROM MEMBER), ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, member.getLogin_id());
			stmt.setString(2, member.getName());
			stmt.setString(3, member.getPassword());
			stmt.setString(4, "testsalt");
			
			cnt = stmt.executeUpdate();
			
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
		return cnt;
	}
	public int updateMember(MemberVO member) {
		int cnt = -1;
		try {
			conn = JDBCConnection.getConenction();
			conn.setAutoCommit(false);
			String sql = "UPDATE MEMBER SET NAME=?, PASSWORD=? WHERE LOGIN_ID = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, member.getName());
			stmt.setString(2, member.getPassword());
			stmt.setString(3, member.getLogin_id());
			
			cnt = stmt.executeUpdate();
			
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
		return cnt;
	}
}
