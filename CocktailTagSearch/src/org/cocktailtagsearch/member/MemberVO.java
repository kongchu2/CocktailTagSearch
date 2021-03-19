package org.cocktailtagsearch.member;


public class MemberVO {
	private int member_id;
	private String login_id;
	private String name;
	private String password;
	private char permission;
	public int getMember_id() {
		return member_id;
	}
	public void setMember_id(int id) {
		this.member_id = id;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPermission(char permission) {
		this.permission = permission;
	}
	public char getPermission() {
		return permission;
	}
	@Override
	public String toString() {
		return "MemberVO [member_id=" + member_id + ", login_id=" + login_id + ", name=" + name + ", password="
				+ password + "]";
	}
	
}
