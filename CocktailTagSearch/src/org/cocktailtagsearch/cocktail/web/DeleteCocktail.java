package org.cocktailtagsearch.cocktail.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cocktailtagsearch.cocktail.CocktailQuerier;
import org.json.simple.JSONObject;

@WebServlet("/DeleteCocktail")
public class DeleteCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		int cocktailId = Integer.parseInt(request.getParameter("cocktailId"));
		
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(false);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if((char)session.getAttribute("permission") == '1') {
			CocktailQuerier dao = new CocktailQuerier();
			if(dao.DeleteCocktail(cocktailId) > 0) {
				map.put("isDeleted", "1");
			} else {
				map.put("isDeleted", "0");
			}
		}
		JSONObject json = new JSONObject(map);
		
		out.print(json);
		out.close();
		
}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
