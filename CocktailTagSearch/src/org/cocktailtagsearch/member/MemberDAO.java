package org.cocktailtagsearch.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.cocktailtagsearch.favoritecocktail.FavoriteCocktailDAO;
import org.cocktailtagsearch.favoritetag.FavoriteTagsDAO;
import org.cocktailtagsearch.util.JDBCConnection;


public class MemberDAO {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	public MemberVO getMember(String login_id) {
		MemberVO member = null;
		try {
			conn = JDBCConnection.getConnection();
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
				member.setPermission(rs.getString("PERMISSIONS").charAt(0));
			} 
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return member;
	}
	public ArrayList<MemberVO> getMemberList() {
		ArrayList<MemberVO> memberList = new ArrayList<MemberVO>();
		MemberVO member = null;
		try {
			conn = JDBCConnection.getConnection();
			String sql = "SELECT * FROM MEMBER";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				member = new MemberVO();
				member.setMember_id(rs.getInt("MEMBER_ID"));
				member.setLogin_id(rs.getString("LOGIN_ID"));
				member.setName(rs.getString("NAME"));
				member.setPassword(rs.getString("PASSWORD"));
				member.setPermission(rs.getString("PERMISSIONS").charAt(0));
				
				memberList.add(member);
			} 
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return memberList;
	}
	public MemberVO loginCheck(String id, String pw) {
		MemberVO member = null;
		try {
			conn = JDBCConnection.getConnection();
			String sql = "SELECT * FROM MEMBER WHERE LOGIN_ID=? AND PASSWORD=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, pw);
			rs = stmt.executeQuery();
			if(rs.next()) {
				member = new MemberVO();
				member.setMember_id(rs.getInt("MEMBER_ID"));
				member.setLogin_id(rs.getString("LOGIN_ID"));
				member.setName(rs.getString("NAME"));
				member.setPassword(rs.getString("PASSWORD"));
				member.setPermission(rs.getString("PERMISSIONS").charAt(0));
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
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO MEMBER VALUES((SELECT nvl(max(MEMBER_ID), 0)+1 FROM MEMBER), ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, member.getLogin_id());
			stmt.setString(2, member.getName());
			stmt.setString(3, member.getPassword());
			stmt.setString(4, "testsalt");
			stmt.setString(5, Character.toString(member.getPermission()));
			
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
			conn = JDBCConnection.getConnection();
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
	public boolean switchlikePost(int memberId, int cocktailId) {
		boolean like = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, cocktailId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				sql = "DELETE FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
			} else {
				like = true;
				sql = "INSERT INTO FAVORITE_COCKTAIL VALUES(?, ?)";
			}
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, cocktailId);
			stmt.executeUpdate();
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
		return like;
	}
	public boolean isLikedPost(int memberId, int cocktailId) {
		boolean like = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, cocktailId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				like = true;
			}
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
		return like;
	}
	public boolean switchlikeTag(int memberId, int tagId) {
		boolean like = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT * FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, tagId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				sql = "DELETE FROM FAVORITE_TAGS WHERE MEMBER_ID=? AND TAG_ID=?";
			} else {
				like = true;
				sql = "INSERT INTO FAVORITE_TAGS VALUES(?, ?)";
			}
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			stmt.setInt(2, tagId);
			stmt.executeUpdate();
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
		return like;
	}
	public boolean authPassword(int memberId, String password) {
		boolean isRightPassword = false;
		try {
			conn = JDBCConnection.getConnection();
			String sql = "SELECT * FROM MEMBER WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, memberId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				isRightPassword = rs.getString("PASSWORD").equals(password);
			}
		}  catch(Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}
		return isRightPassword;
	}
	public boolean updateMemberName(int memberId, String name) {
		boolean isUpdated = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "UPDATE MEMBER SET NAME=? WHERE MEMBER_ID = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, name);
			stmt.setInt(2, memberId);
			
			int cnt = stmt.executeUpdate();
			isUpdated = cnt > 0;
			
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
		return isUpdated;
	}
	public boolean updateMemberPassword(int memberId, String password) {
		boolean isUpdated = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "UPDATE MEMBER SET PASSWORD=? WHERE MEMBER_ID = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, password);
			stmt.setInt(2, memberId);
			
			int cnt = stmt.executeUpdate();
			isUpdated = cnt > 0;
			
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
		return isUpdated;
	}
	public boolean deleteMember(int memberId) {
		boolean delete = false;
		try {
			conn = JDBCConnection.getConnection();
			conn.setAutoCommit(false);
			
			FavoriteCocktailDAO FCDao = new FavoriteCocktailDAO();
			FavoriteTagsDAO FTDao = new FavoriteTagsDAO();
			
			FCDao.deleteFavoriteCocktailByMember_id(memberId);
			FTDao.deleteFavoriteTagByMember_id(memberId);
			
			String sql = "DELETE FROM MEMBER WHERE MEMBER_ID=?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, memberId);
			
			int cnt = stmt.executeUpdate();
			delete = cnt > 0;
			
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
		return delete;
	}
}
