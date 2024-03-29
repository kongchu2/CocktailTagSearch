package org.cocktailtagsearch.web.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.tag.TagCache;
import org.cocktailtagsearch.tag.TagVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/TagSearch")
public class TagSearch extends HttpServlet {
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
			int tagId = ((Long)tagArray.get(i)).intValue();
			tagIdList.add(tagId);
		}
		
		ArrayList<TagVO> tagList = null;
		if(tagIdList.size() == 0) {
			if(searchStr.equals("")) 
				tagList = TagCache.getTagList();
			else
				tagList = TagCache.getSearchedTagList(searchStr);
		} else {
			if(searchStr.equals("")) 
				tagList = TagCache.getTagListWithoutTagIdList(tagIdList);
			else
				tagList = TagCache.getSearchedTagListWithoutTagIdList(searchStr, tagIdList);
		}
		JSONObject json = null;
		JSONArray tags = new JSONArray();
		
		boolean isExist = false;

		HashMap<String, Object> hashMap = null;
		JSONObject tagJson = null;
		
		for(TagVO tag : tagList) {
			hashMap = new HashMap<String, Object>();
			
			hashMap.put("id", tag.getId());
			hashMap.put("name", tag.getName());
			
			tagJson = new JSONObject(hashMap);
			tags.add(tagJson);
			isExist = true;
		}
		hashMap = new HashMap<String, Object>();
		hashMap.put("tags", tags);
		json = new JSONObject(hashMap);

		if(isExist) {
			out.print(json);
		} else {
			out.print("");
		}
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
