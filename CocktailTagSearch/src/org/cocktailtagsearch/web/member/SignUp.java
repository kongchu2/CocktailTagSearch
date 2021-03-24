package org.cocktailtagsearch.web.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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
import org.cocktailtagsearch.util.RsaDecryption;


@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String login_id	= request.getParameter("df8Z368CKkFDNHk7");
		String name	= request.getParameter("tFw9C8dV2KGBhbrY");
		String pw	= request.getParameter("wGKnr4ppPF8rBPss");
		
		PrivateKey privateKey = RsaDecryption.SESSION_KEY;
        
        try {
			login_id = RsaDecryption.decryptRsa(privateKey, login_id);
			pw = RsaDecryption.decryptRsa(privateKey, pw);
			name = RsaDecryption.decryptRsa(privateKey, name);
        } catch (Exception e) {
			e.printStackTrace();
		}

		RsaDecryption.SESSION_KEY = null;
		
		
		MemberDAO dao = new MemberDAO();
		MemberVO vo = dao.getMember(login_id);
		
		PrintWriter out = response.getWriter();
		
		if(vo != null) {
			out.print("overlap");
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
				out.print("success");
			} else {
				out.print("fail");
			}	
		}
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
