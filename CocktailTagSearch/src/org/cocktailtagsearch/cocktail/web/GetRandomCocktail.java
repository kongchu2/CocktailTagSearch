package org.cocktailtagsearch.cocktail.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.cocktail.CocktailQuerier;


@WebServlet("/GetRandomCocktail")
public class GetRandomCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		CocktailQuerier cq = new CocktailQuerier();
		int maxId = cq.getMaxId();
		int cocktailId = (int)((Math.random()) * maxId) + 1;
		out.print(cocktailId);
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
