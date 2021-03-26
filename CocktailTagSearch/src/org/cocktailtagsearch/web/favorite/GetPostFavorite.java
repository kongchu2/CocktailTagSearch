package org.cocktailtagsearch.web.favorite;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.member.MemberQuerier;
import org.json.simple.JSONObject;

@WebServlet("/GetPostLike")
public class GetPostFavorite extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		 
		int cocktailId = Integer.parseInt(request.getParameter("cocktailId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		
		MemberQuerier dao = new MemberQuerier();
		boolean isLiked = dao.isLikedPost(userId, cocktailId);
		
	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("isLiked", isLiked ? "1":"0");
		JSONObject json = new JSONObject(map);
		
		out.print(json);
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
