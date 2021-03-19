	package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Member.MemberDAO;
import Member.MemberVO;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		HttpSession session = request.getSession(true);
		
		session.setMaxInactiveInterval(10*60);
		
		MemberDAO dao = new MemberDAO();
		MemberVO member = dao.loginCheck(id, pw);
		
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
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
