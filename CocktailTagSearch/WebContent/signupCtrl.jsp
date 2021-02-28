
<%@page import="Member.MemberVO"%>
<%@page import="Member.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session = "false"%>
<%@ page import="java.sql.*" %>
<%
	request.setCharacterEncoding("UTF-8");

	String login_id	= request.getParameter("id");
	String name	= request.getParameter("name");
	String pw	= request.getParameter("pw");
	
	MemberDAO dao = new MemberDAO();
	MemberVO vo = dao.getMember(login_id);
	
	if(vo != null) {
		out.print("<script>alert(\"아이디가 중복되었습니다.\");history.go(-1);</script>");
	} else {
		MemberVO newMember = new MemberVO();
		newMember.setLogin_id(login_id);
		newMember.setName(name);
		newMember.setPassword(pw);
		newMember.setPermission('0');
		int cnt = dao.addMember(newMember);
		if(cnt > 0) {
			out.print("<script>alert(\"성공\");</script>");
		} else {
			out.print("<script>alert(\"실패\");</script>");
		}
		response.sendRedirect("index.html");
	}
	
%>