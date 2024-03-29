package org.cocktailtagsearch.web.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

@WebServlet("/SessionData")
public class SessionData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		if(session == null) {
			map.put("signed", "0");
		} else {
			map.put("signed", "1");
			String name = (String) session.getAttribute("userName");
			String login_id = (String) session.getAttribute("userLogin_id");
			int id = (Integer) session.getAttribute("userId");
			char permission = (char)session.getAttribute("permission");
			
			HashMap<String, Object> user = new HashMap<String, Object>();
			user.put("login_id", login_id);
			user.put("id", id);
			user.put("name", name);
			user.put("permission", permission);
			map.put("user", user);
		}
		out.print(new JSONObject(map));
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
