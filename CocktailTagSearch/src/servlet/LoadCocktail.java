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

import Cocktail.CocktailDAO;
import Cocktail.CocktailVO;
import Tag.TagVO;

@WebServlet("/LoadCocktail")
public class LoadCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		PrintWriter out = response.getWriter();
		
		CocktailDAO dao = new CocktailDAO();
		ArrayList<CocktailVO> cocktailList = null;
		cocktailList = dao.getCocktailList();
		

		String json = null;
		HashMap<String, Object> hashMap = null;
		
		JSONArray cocktailArray = new JSONArray();
		for(CocktailVO cocktail : cocktailList) {
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