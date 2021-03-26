package org.cocktailtagsearch.favoritetag.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.favoritetag.FavoriteTagsDAO;
import org.json.simple.JSONObject;


@WebServlet("/AddTagLike")
public class AddFavoriteTag extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		int tagId = Integer.parseInt(request.getParameter("tagId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		
		FavoriteTagsDAO dao = new FavoriteTagsDAO();
		boolean isLiked = dao.switchlikeTag(userId, tagId);
		
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
