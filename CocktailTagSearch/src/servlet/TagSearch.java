package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Cocktail.CocktailDAO;
import Cocktail.CocktailVO;
import Tag.TagVO;

@WebServlet("/TagSearch")
public class TagSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("json");
		PrintWriter out = response.getWriter();
		
		String searchStr = request.getParameter("search");
		String tagStr = request.getParameter("tags");
		
		JSONArray tagArray = null;
		try {
			JSONParser parser = new JSONParser();
			tagArray = (JSONArray)parser.parse(tagStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> tagIdList = new ArrayList<Integer>();
		
		for(int i=0;i<tagArray.size();i++) {
			Object tag = tagArray.get(i);
			if(tag instanceof JSONObject) {
				Object tagIdObject = ((JSONObject) tag).get("id");
				if(tagIdObject instanceof java.lang.Long) {
					tagIdList.add(((Long)tagIdObject).intValue());
				}
			}
		}
		
		CocktailDAO dao = new CocktailDAO();
		ArrayList<CocktailVO> cocktailList = null;
		
		if(tagIdList.size() == 0)
			cocktailList = dao.getCocktailList();
		else
			cocktailList = dao.getCocktailListByTagList(tagIdList);
		
		JSONObject json = new JSONObject();
		JSONArray cocktailArray = new JSONArray();
		
		for(CocktailVO cocktail : cocktailList) {
			if(cocktail.getName().contains(searchStr)) {
				JSONObject cocktailJson = new JSONObject();
				cocktailJson.put("id", cocktail.getId());
				cocktailJson.put("name", cocktail.getName());
				cocktailJson.put("image", cocktail.getImage());
				cocktailJson.put("desc", cocktail.getDesc());
				cocktailJson.put("history", cocktail.getHistory());
				cocktailJson.put("taste", cocktail.getTaste());
				cocktailJson.put("base", cocktail.getBase());
				cocktailJson.put("build", cocktail.getBuild());
				cocktailJson.put("glass", cocktail.getGlass());
				JSONArray tempTagArray = new JSONArray();
				for(TagVO tag : cocktail.getTagList()) {
					JSONObject tagJson = new JSONObject();
					tagJson.put("id", tag.getId());
					tagJson.put("name", tag.getName());
					tagJson.put("desc", tag.getDesc());
					tagJson.put("category", tag.getCategory());
					tempTagArray.add(tagJson);
				}
				cocktailJson.put("tags", tempTagArray);
				cocktailArray.add(cocktailJson);
			}
		}
		
		json.put("cocktails", cocktailArray);
		
		System.out.println(json);
		
		out.print(json);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
