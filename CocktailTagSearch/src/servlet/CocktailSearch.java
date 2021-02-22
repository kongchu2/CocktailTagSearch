package servlet;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

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
		else {
			ArrayList<CocktailVO> tempCocktailList = dao.getCocktailListByTagList(tagIdList);
			cocktailList = new ArrayList<CocktailVO>();
			int save_before_cocktail_id = 0;
			for(CocktailVO cocktail : tempCocktailList) {
				int count = 0;
				boolean flag = true;
				
				ArrayList<TagVO> tempTagList = cocktail.getTagList();
				
				if(save_before_cocktail_id == cocktail.getId())
					continue;
				save_before_cocktail_id = cocktail.getId();

				for(TagVO tag : tempTagList) {
					if(tagIdList.contains(tag.getId()))
						count++;
				}
				if(count < tagIdList.size()) 
					flag = false;
				if(flag) 
					cocktailList.add(cocktail);
			}
		}
		String json = null;
		HashMap<String, Object> hashMap = null;
		
		JSONArray cocktailArray = new JSONArray();
		int cocktailCount = 0;
		int limit = 100; // 최대로 보여줄 칵테일 수
		for(CocktailVO cocktail : cocktailList) {
			if(cocktail.getName().contains(searchStr) && cocktailCount < limit) {
				cocktailCount++;
				hashMap = new HashMap<String, Object>();
				hashMap.put("id", cocktail.getId());
				hashMap.put("name", cocktail.getName());
				hashMap.put("image", cocktail.getImage());
				hashMap.put("desc", cocktail.getDesc());
				hashMap.put("history", cocktail.getHistory());
				hashMap.put("taste", cocktail.getTaste());
				hashMap.put("base", cocktail.getBase());
				hashMap.put("build", cocktail.getBuild());
				hashMap.put("glass", cocktail.getGlass());

				
				JSONArray tempTagArray = new JSONArray();
				JSONObject tagJson = null;
				HashMap<String, Object> tempHashMap = null;
				for(TagVO tag : cocktail.getTagList()) {
					tempHashMap = new HashMap<String, Object>();
					
					tempHashMap.put("id", tag.getId());
					tempHashMap.put("name", tag.getName());
					tempHashMap.put("desc", tag.getDesc());
					tempHashMap.put("category", tag.getCategory());
					
					tagJson = new JSONObject(tempHashMap);
					tempTagArray.add(tagJson);
				}
				
				hashMap.put("tags", tempTagArray);
				
				JSONObject cocktailJson = new JSONObject(hashMap);
				cocktailArray.add(cocktailJson);
				
			}
			
			hashMap = null;
		}
		
		if(cocktailArray.size() > 0) {
			json = "{\"cocktails\":[";
			for(int i=0; i<cocktailArray.size(); i++) {
				if(i>0) json += ",";
				json += cocktailArray.get(i);
			}
			json += "]}";
		}
		
		out.print(json);
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}