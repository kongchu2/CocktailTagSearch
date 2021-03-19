package org.cocktailtagsearch.web.search;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.cocktail.CocktailQuerier;
import org.cocktailtagsearch.cocktail.CocktailVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/CocktailSearch")
public class CocktailSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		PrintWriter out = response.getWriter();
		
		String searchStr = request.getParameter("search").trim();
		String tagStr = request.getParameter("tags");
		int cocktailLength = Integer.parseInt(request.getParameter("length"));
		
		JSONArray tagArray = null;
		try {
			JSONParser parser = new JSONParser();
			tagArray = (JSONArray)parser.parse(tagStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> tagIdList = new ArrayList<Integer>();
		
		for(int i=0;i<tagArray.size();i++) {
			int tagId = ((Long)((JSONObject)tagArray.get(i)).get("id")).intValue();
			tagIdList.add(tagId);
		}
		
		CocktailQuerier dao = new CocktailQuerier();
		ArrayList<CocktailVO> cocktailList = null;
		
		if(tagIdList.size() == 0) {
			if(searchStr.equals(""))
				cocktailList = dao.getCocktailList(cocktailLength);
			else
				cocktailList = dao.getSearchedCocktailList(searchStr, cocktailLength);
		} else {
			if(searchStr.equals(""))
				cocktailList = dao.getCocktailListByTagList(tagIdList, cocktailLength);
			else
				cocktailList = dao.getSearchedCocktailListByTagList(searchStr, tagIdList, cocktailLength);
		}
		JSONArray cocktailArray = new JSONArray();
		
		for(CocktailVO cocktail : cocktailList) {
			JSONObject cocktailJson = new JSONObject(cocktail.toHashMapNeedToSearch());
			cocktailArray.add(cocktailJson);
		}
		JSONObject json = new JSONObject();
		json.put("cocktails", cocktailArray);
		out.print(json);
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}