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

import Tag.TagDAO;
import Tag.TagVO;

@WebServlet("/search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String searchStr = request.getParameter("search");
		
		TagDAO tag_dao = new TagDAO();
		ArrayList<TagVO> tagList = tag_dao.getTagList();
		
		JSONObject json = new JSONObject();
		JSONArray tags = new JSONArray();
		
		boolean isExist = false;
		for(TagVO tag : tagList) {
			if(tag.getName().contains(searchStr)) {
				JSONObject tagJson = new JSONObject();
				tagJson.put("id", tag.getId());
				tagJson.put("name", tag.getName());
				tags.add(tagJson);
				isExist = true;
			}
		}
		json.put("tag", tags);
		
		System.out.println(json);
		
		response.setContentType("json");

		PrintWriter out = response.getWriter(); 
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
