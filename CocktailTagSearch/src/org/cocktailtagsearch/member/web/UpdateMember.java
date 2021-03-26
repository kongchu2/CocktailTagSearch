package org.cocktailtagsearch.member.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.PrivateKey;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.member.MemberQuerier;
import org.cocktailtagsearch.util.security.PasswordHash;
import org.cocktailtagsearch.util.security.RsaDecryption;

@WebServlet("/updateMember")
public class UpdateMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String type = request.getParameter("type");
		int memberId = Integer.parseInt(request.getParameter("memberId"));
		
		PrivateKey privateKey = RsaDecryption.SESSION_KEY;
		
        RsaDecryption.SESSION_KEY = null;

		MemberQuerier dao = new MemberQuerier();
		
		boolean isUpdated = false;
		
		if(type.equals("name")) {
			String name = request.getParameter("name");
			isUpdated = dao.updateMemberName(memberId, name);
		} else {

			String id = request.getParameter("encrypt_id");
			String pw = request.getParameter("encrypt_pw");
			
			try {                      
				pw = RsaDecryption.decryptRsa(privateKey, pw);
				id = RsaDecryption.decryptRsa(privateKey, id);
	        } catch (Exception e1) {
				e1.printStackTrace();
			}
			
			String hex = null;
			try {
				hex = PasswordHash.Hashing(pw, dao.getSalt(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			isUpdated = dao.updateMemberPassword(memberId, hex);
		}
		
		if(isUpdated) {
			out.print("<script>alert(\"성공적으로 변경되었습니다\");</script>");
			response.sendRedirect("myPage.html");
		} else {
			out.print("<script>alert(\"실패하였습니다.\");</script>");
			response.sendRedirect("myPage.html");
		}
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
