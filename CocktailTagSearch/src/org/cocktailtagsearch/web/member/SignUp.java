package org.cocktailtagsearch.web.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.member.MemberDAO;
import org.cocktailtagsearch.member.MemberVO;
import org.cocktailtagsearch.util.PasswordHash;


@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String login_id	= request.getParameter("id");
		String name	= request.getParameter("name");
		String pw	= request.getParameter("pw");
		
		MemberDAO dao = new MemberDAO();
		MemberVO vo = dao.getMember(login_id);
		
		PrintWriter out = response.getWriter();
		
		if(vo != null) {
			out.print("<script>alert(\"아이디가 중복되었습니다.\");history.go(-1);</script>");
		} else {

			MemberVO newMember = new MemberVO();
			newMember.setLogin_id(login_id);
			newMember.setName(name);
			newMember.setPermission('0');
			try {
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				
				byte[] bytes = new byte[22];
				random.nextBytes(bytes);
				String salt = new String(Base64.getEncoder().encode(bytes));
				newMember.setSalt(salt);
				
				String hex = null;
				
				hex = PasswordHash.Hashing(pw, salt);
				
				newMember.setPassword(hex);				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			int cnt = dao.addMember(newMember);
			
			if(cnt > 0) {
				out.print("<script>alert(\"성공\");</script>");
			} else {
				out.print("<script>alert(\"실패\");</script>");
			}
			response.sendRedirect("index.html");
		}
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
