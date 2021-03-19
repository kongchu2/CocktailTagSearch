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

import Tag.TagQuerier;
import Tag.TagVO;

@WebServlet("/MatchingTag")
public class MatchingTag extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		String nameStr = request.getParameter("name");
		
		TagQuerier dao = new TagQuerier();
		
		TagVO tag = dao.getSearchedTagJustOne(nameStr);

		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("id", tag.getId());
		hashMap.put("name", tag.getName());
		
		JSONObject tagJson = new JSONObject(hashMap);
		
		hashMap = new HashMap<String, Object>();
		hashMap.put("tags", tagJson);
		
		JSONObject json = new JSONObject(hashMap);

		out.print(json);
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
