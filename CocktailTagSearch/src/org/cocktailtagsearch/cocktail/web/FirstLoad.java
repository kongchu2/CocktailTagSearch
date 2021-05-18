package org.cocktailtagsearch.cocktail.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.cocktailtagsearch.cocktail.CocktailCache;
import org.cocktailtagsearch.cocktail.CocktailQuerier;

@WebServlet(name="Firstload", loadOnStartup = 2, urlPatterns = {"/FirstLoad"})
public class FirstLoad extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
	    	CocktailQuerier dao = new CocktailQuerier();
			CocktailCache.CocktailList = dao.getCocktailList();
			System.out.println("실행됨");
	}

}
