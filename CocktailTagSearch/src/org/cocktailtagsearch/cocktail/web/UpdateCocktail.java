package org.cocktailtagsearch.cocktail.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.cocktail.CocktailQuerier;
import org.cocktailtagsearch.cocktail.CocktailVO;
import org.cocktailtagsearch.tag.TagQuerier;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@WebServlet("/UpdateCocktail")
public class UpdateCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String cocktailData = request.getParameter("cocktail");
		String tagStr = request.getParameter("tag");
		
		JSONParser parser = new JSONParser();
		JSONObject cocktailJSON = null;
		
		try {
			cocktailJSON = (JSONObject)parser.parse(cocktailData);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		JSONArray tagArray = null;
		try {
			
			tagArray = (JSONArray)parser.parse(tagStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> tagIdList = new ArrayList<Integer>();
				
		for(int i=0;i<tagArray.size();i++) {
			int tagId = ((Long)((JSONObject)tagArray.get(i)).get("id")).intValue();
			tagIdList.add(tagId);
		}
		
		CocktailVO cocktail = new CocktailVO();
		cocktail.setName((String) cocktailJSON.get("name"));
		cocktail.setImage((String) cocktailJSON.get("image"));
		cocktail.setDesc((String) cocktailJSON.get("desc"));
		cocktail.setHistory((String) cocktailJSON.get("history"));
		cocktail.setTaste((String) cocktailJSON.get("taste"));
		cocktail.setBase((String) cocktailJSON.get("base"));
		cocktail.setBuild((String) cocktailJSON.get("build"));
		cocktail.setGlass((String) cocktailJSON.get("glass"));
		
		TagQuerier tag_dao = new TagQuerier();
		cocktail.setTagList(tag_dao.getTagListByTagIdList(tagIdList));
		
		CocktailQuerier cocktail_dao = new CocktailQuerier();
		boolean isUpdated = cocktail_dao.UpdateCocktail(cocktail,  Integer.parseInt((String)cocktailJSON.get("id")));
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("isUpdated", isUpdated ? "1":"0");
		out.print(new JSONObject(map));
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
