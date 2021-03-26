	package org.cocktailtagsearch.web.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.PrivateKey;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cocktailtagsearch.member.MemberQuerier;
import org.cocktailtagsearch.member.MemberVO;
import org.cocktailtagsearch.util.security.PasswordHash;
import org.cocktailtagsearch.util.security.RsaDecryption;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String id = request.getParameter("HxxKFRVJZqxcft8V");
		String pw = request.getParameter("evyv6StCkKAvwEDu");
		
		
		HttpSession session = request.getSession(true);
		
		session.setMaxInactiveInterval(10*60);
 
        PrivateKey privateKey = RsaDecryption.SESSION_KEY;
        
        try {
			id = RsaDecryption.decryptRsa(privateKey, id);                        
			pw = RsaDecryption.decryptRsa(privateKey, pw);
        } catch (Exception e1) {
			e1.printStackTrace();
		}

        RsaDecryption.SESSION_KEY = null;

		MemberQuerier dao = new MemberQuerier();
		String hex = null;
		try {
			hex = PasswordHash.Hashing(pw, dao.getSalt(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(hex == null) {
			session.invalidate();
			out.print("wrong");
		}
		else {
			MemberVO member = dao.loginCheck(id, hex);
			if(member != null) {
				session.setAttribute("userId", member.getMember_id());
				session.setAttribute("userLogin_id", member.getLogin_id());
				session.setAttribute("userName", member.getName());
				session.setAttribute("permission", member.getPermission());
				out.print("right");
			} else {
				session.invalidate();
				out.print("wrong");
			}
		}
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
