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
			int tagId = ((Long)((JSONObject)tagArray.get(i)).get("id")).intValue();
			tagIdList.add(tagId);
		}
		
		CocktailDAO dao = new CocktailDAO();
		ArrayList<CocktailVO> cocktailList = null;

		if(tagIdList.size() == 0)
			cocktailList = dao.getCocktailList();
		else {
			cocktailList = dao.getCocktailListByTagList(tagIdList);
		}
		HashMap<String, Object> hashMap = null;
		
		JSONArray cocktailArray = new JSONArray();
		for(CocktailVO cocktail : cocktailList) {
			if(cocktail.getName().contains(searchStr)) {
				JSONObject cocktailJson = new JSONObject(cocktail.toHashMap());
				cocktailArray.add(cocktailJson);
			}
			hashMap = null;
		}
		JSONObject json = new JSONObject();
		json.put("cocktails", cocktailArray);
		out.print(json);
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}