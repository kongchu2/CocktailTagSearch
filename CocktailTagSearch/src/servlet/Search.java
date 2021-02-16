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

@WebServlet("/search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		String searchStr = request.getParameter("search");
		
		TagDAO tag_dao = new TagDAO();
		ArrayList<TagVO> tagList = tag_dao.getTagList();
		
		JSONObject json = null;
		JSONArray tags = new JSONArray();
		
		boolean isExist = false;

		HashMap<String, Object> hashMap = null;
		JSONObject tagJson = null;
		
		for(TagVO tag : tagList) {
			if(tag.getName().contains(searchStr)) {
				hashMap = new HashMap<String, Object>();
				
				hashMap.put("id", tag.getId());
				hashMap.put("name", tag.getName());
				
				tagJson = new JSONObject(hashMap);
				tags.add(tagJson);
				isExist = true;
			}
			hashMap = null;
		}
		hashMap = new HashMap<String, Object>();
		hashMap.put("tags", tags);
		json = new JSONObject(hashMap);

		if(isExist) {
			out.print(json);
		} else {
			out.print("");
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
