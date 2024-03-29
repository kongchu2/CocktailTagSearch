package org.cocktailtagsearch.web.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cocktailtagsearch.member.MemberQuerier;
import org.cocktailtagsearch.util.security.PasswordHash;
import org.cocktailtagsearch.util.security.RsaDecryption;


@WebServlet("/PasswordAuth")
public class PasswordAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(false);
		
		
		if(session == null) {
			response.sendRedirect("login.html");
			out.print("0");
		} else {
			String pw	= request.getParameter("fXVA92VbkUVvHGNq");
			
			PrivateKey privateKey = RsaDecryption.SESSION_KEY;
	        
	        try {
				pw = RsaDecryption.decryptRsa(privateKey, pw);
	        } catch (Exception e) {
				e.printStackTrace();
			}

			RsaDecryption.SESSION_KEY = null;
			
			int memberId = (int) session.getAttribute("userId");
			MemberQuerier dao = new MemberQuerier();
			
			String hex = null;
			try {
				hex = PasswordHash.Hashing(pw, dao.getSalt(memberId));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if(dao.authPassword(memberId, hex)) {
				out.print("1");
			} else {
				out.print("0");
			}
		}
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
