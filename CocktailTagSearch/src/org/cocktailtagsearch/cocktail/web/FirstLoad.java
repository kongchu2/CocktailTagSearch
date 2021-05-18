package org.cocktailtagsearch.cocktail.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.cocktailtagsearch.cocktail.CocktailCache;
import org.cocktailtagsearch.tag.TagCache;

@WebServlet(name="Firstload", loadOnStartup = 2, urlPatterns = {"/FirstLoad"})
public class FirstLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	public void init(ServletConfig config) throws ServletException {
	    CocktailCache.init();
	    TagCache.init();
		System.out.println("실행됨");
	}

}
