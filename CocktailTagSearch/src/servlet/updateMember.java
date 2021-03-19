package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Member.MemberDAO;

@WebServlet("/updateMember")
public class updateMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String type = request.getParameter("type");
		int memberId = Integer.parseInt(request.getParameter("memberId"));
		
		MemberDAO dao = new MemberDAO();
		
		boolean isUpdated;
		
		if(type.equals("name")) {
			String name = request.getParameter("name");
			isUpdated = dao.updateMemberName(memberId, name);
		} else {
			String pw = request.getParameter("pw");
			isUpdated = dao.updateMemberPassword(memberId, pw);
		}
		
		out.print("<script>alert(\"성공적으로 변경되었습니다\");</script>");
		response.sendRedirect("myPage.html");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
