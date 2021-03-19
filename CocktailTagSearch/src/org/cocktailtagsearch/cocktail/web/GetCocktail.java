package org.cocktailtagsearch.cocktail.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.cocktail.CocktailQuerier;
import org.cocktailtagsearch.cocktail.CocktailVO;
import org.json.simple.JSONObject;


@WebServlet("/GetCocktail")
public class GetCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json; charset=UTF-8;");
		
		int cocktail_id = Integer.parseInt(request.getParameter("id"));
		CocktailQuerier cq = new CocktailQuerier();
		CocktailVO vo = cq.getCocktail(cocktail_id);
		JSONObject cocktailJSON = vo.toJSON();
		
		out.print(cocktailJSON);
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
