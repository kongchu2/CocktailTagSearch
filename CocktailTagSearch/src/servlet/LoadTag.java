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

import Tag.TagDAO;
import Tag.TagVO;

@WebServlet("/LoadTag")
public class LoadTag extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		PrintWriter out = response.getWriter();
		
		TagDAO dao = new TagDAO();
		ArrayList<TagVO> TagList = null;
		TagList = dao.getTagList();
		

		String json = null;
		HashMap<String, Object> hashMap = null;
		JSONObject cocktailJson = null;
		
		JSONArray cocktailArray = new JSONArray();
		for(TagVO tag : TagList) {
			hashMap = new HashMap<String, Object>();
			hashMap.put("id", tag.getId());
			hashMap.put("name", tag.getName());
			hashMap.put("desc", tag.getDesc());
			hashMap.put("category", tag.getCategory());
			
			cocktailJson = new JSONObject(hashMap);
			cocktailArray.add(cocktailJson);
		}
		
		if(cocktailArray.size() > 0) {
			json = "{\"tags\":[";
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