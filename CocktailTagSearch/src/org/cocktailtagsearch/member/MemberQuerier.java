package org.cocktailtagsearch.member;

import java.util.ArrayList;
import java.util.HashMap;

import org.cocktailtagsearch.db.access.DAO;
import org.cocktailtagsearch.favoritecocktail.FavoriteCocktailQuerier;
import org.cocktailtagsearch.favoritetag.FavoriteTagsQuerier;
import org.cocktailtagsearch.util.MapParser;


public class MemberQuerier {
	private DAO dao = new DAO();
	
	public MemberVO getMember(String login_id) {
		MemberVO member = null;
		String sql = "SELECT * FROM MEMBER WHERE LOGIN_ID=?";
		ArrayList<MemberVO> list = MapParser.toMemberList(dao.executeSQL(sql, login_id));
		if(list.size() > 0)
			member = list.get(0);
		return member;
	}
	public MemberVO loginCheck(String id, String pw) {
		MemberVO member = null;
		String sql = "SELECT * FROM MEMBER WHERE LOGIN_ID=? AND PASSWORD=?";
		ArrayList<MemberVO> list = MapParser.toMemberList(dao.executeSQL(sql, id, pw));
		if(list.size() > 0) {
			member = list.get(0);
		}
		return member;
	}
	public ArrayList<MemberVO> getMemberList() {
		ArrayList<MemberVO> memberList = new ArrayList<MemberVO>();
		String sql = "SELECT * FROM MEMBER";
		memberList = MapParser.toMemberList(dao.executeSQL(sql));
		return memberList;
	}
	public int addMember(MemberVO member) {
		int cnt = -1;
		String sql = "INSERT INTO MEMBER VALUES((SELECT nvl(max(MEMBER_ID), 0)+1 FROM MEMBER), ?, ?, ?, ?, ?)";
		cnt = dao.executeUpdateSQL(sql, member.getLogin_id(), member.getName(), member.getPassword(), member.getSalt(), Character.toString(member.getPermission()));
		return cnt;
	}
	public int updateMember(MemberVO member) {
		int cnt = -1;
		String sql = "UPDATE MEMBER SET NAME=?, PASSWORD=? WHERE LOGIN_ID = ?";
		cnt = dao.executeUpdateSQL(sql, member.getName(), member.getPassword(), member.getLogin_id());
		return cnt;
	}
	
	public boolean isLikedPost(int memberId, int cocktailId) {
		boolean like = false;
		String sql = "SELECT * FROM FAVORITE_COCKTAIL WHERE MEMBER_ID=? AND COCKTAIL_ID=?";
		like = dao.executeSQL(sql, memberId, cocktailId).size() != 0;
		return like;
	}
	public boolean authPassword(int memberId, String password) {
		boolean isRightPassword = false;
		String sql = "SELECT PASSWORD FROM MEMBER WHERE MEMBER_ID=?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, memberId);
		if(list.size() > 0)
			isRightPassword = list.get(0).get("PASSWORD").equals(password);
		return isRightPassword;
	}
	public boolean updateMemberName(int memberId, String name) {
		boolean isUpdated = false;
		String sql = "UPDATE MEMBER SET NAME=? WHERE MEMBER_ID = ?";
		isUpdated = dao.executeUpdateSQL(sql, name, memberId) > 0;
		return isUpdated;
	}
	public boolean updateMemberPassword(int memberId, String password) {
		boolean isUpdated = false;
		String sql = "UPDATE MEMBER SET PASSWORD=? WHERE MEMBER_ID = ?";
		isUpdated = dao.executeUpdateSQL(sql, password, memberId) > 0;
		return isUpdated;
	}
	public boolean deleteMember(int memberId) {
		boolean delete = false;
		String sql = "DELETE FROM MEMBER WHERE MEMBER_ID=?";
		
		FavoriteCocktailQuerier FCDao = new FavoriteCocktailQuerier();
		FavoriteTagsQuerier FTDao = new FavoriteTagsQuerier();
		
		FCDao.deleteFavoriteCocktailByMember_id(memberId);
		FTDao.deleteFavoriteTagByMember_id(memberId);		
		
		delete = dao.executeUpdateSQL(sql, memberId) > 0; 
		return delete;
	}
	
	// MEMBER_ID
	public String getSalt(int memberId) {
		String salt = null;
		String sql = "SELECT password_salt FROM MEMBER WHERE MEMBER_ID=?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, memberId);
		if(list.size() > 0)
			salt = (String)list.get(0).get("PASSWORD_SALT");
		return salt;
	}
	// LOGIN_ID
	public String getSalt(String loginId) {
		String salt = null;
		String sql = "SELECT password_salt FROM MEMBER WHERE LOGIN_ID=?";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, loginId);
		if(list.size() > 0) {
			salt = (String)list.get(0).get("PASSWORD_SALT");
		}
		return salt;
	}
}
