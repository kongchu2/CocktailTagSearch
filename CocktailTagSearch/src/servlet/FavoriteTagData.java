package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import FavoriteTags.FavoriteTagsDAO;
import FavoriteTags.FavoriteTagsVO;
import Member.MemberDAO;
import Member.MemberVO;
import Tag.TagDAO;
import Tag.TagVO;

@WebServlet("/FavoriteTagData")
public class FavoriteTagData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String loveStr = request.getParameter("love");
		String tagStr = request.getParameter("tags");
		
		if(loveStr == null || tagStr == null) return;
		
		JSONArray loveArray = null;
		JSONArray tagArray = null;
		try {
			JSONParser parser = new JSONParser();

			loveArray = (JSONArray)parser.parse(loveStr);
			tagArray =  (JSONArray)parser.parse(tagStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

			
		ArrayList<String> loveNameList = new ArrayList<String>();
		if (loveArray != null) { 
		   for (int i=0; i<loveArray.size(); i++){ 
			   loveNameList.add((String)loveArray.get(i));
		   } 
		} 
		
		ArrayList<String> tagNameList = new ArrayList<String>();
		
		for(int i=0;i<tagArray.size();i++) {
			Object tag = tagArray.get(i);
			if(tag instanceof JSONObject) {
				Object tagNameObject = ((JSONObject) tag).get("name");
				if(tagNameObject instanceof java.lang.String) {
					tagNameList.add(((String)tagNameObject));
				}
			}
		}
		
		loveNameList.addAll(tagNameList);
		ArrayList<String> nameList = loveNameList;
		
		JSONObject json = new JSONObject();

		HttpSession session = request.getSession(false);
		
		MemberDAO memberDAO = null;
		MemberVO memberVO = null;
		
		FavoriteTagsDAO favoriteTagsDAO = null;
		TagDAO tagDAO = null;
		
		ArrayList<FavoriteTagsVO> favoriteTagList= null;
		ArrayList<TagVO> tagList = null;

		boolean flag = true;
		String login_id = null;
		if(session != null) {
			login_id = (String) session.getAttribute("userLogin_id");
			if(login_id == null) flag = false;
		} else {
			flag = false;
		}
		
		if(flag) {
			memberDAO = new MemberDAO();
			memberVO = memberDAO.getMember(login_id);

			favoriteTagsDAO = new FavoriteTagsDAO();
			favoriteTagList = favoriteTagsDAO.getFavoriteTagListByMember_id(memberVO.getMember_id());
			
	       	tagDAO = new TagDAO();
			tagList = new ArrayList<TagVO>();
			
			if(loveStr.equals("[]") && tagStr.equals("[]")) {
				for(FavoriteTagsVO favoriteTag : favoriteTagList) {
		       		tagList.add(tagDAO.getTag(favoriteTag.getTag_id()));
		       	}
				json.put("remove", false);
			} else {
				for(FavoriteTagsVO favoriteTag : favoriteTagList) {
					if(!nameList.contains(tagDAO.getTag(favoriteTag.getTag_id()).getName())) {
						tagList.add(tagDAO.getTag(favoriteTag.getTag_id()));
					}
				}
				if(tagList.isEmpty()) {
					for(FavoriteTagsVO favoriteTag : favoriteTagList) {
						if(!tagNameList.contains(tagDAO.getTag(favoriteTag.getTag_id()).getName())) {
							tagList.add(tagDAO.getTag(favoriteTag.getTag_id()));
						}
					}
					System.out.println(tagList);
					json.put("remove", true);
				}
			}
	       	JSONArray array = new JSONArray();
			JSONObject tagJson = null;
			
			for(TagVO tag : tagList) {
				tagJson = new JSONObject();
				
				tagJson.put("id", tag.getId());
				tagJson.put("name", tag.getName());
				tagJson.put("desc", tag.getDesc());
				tagJson.put("category", tag.getCategory());
			
				array.add(tagJson);
			}
			json.put("tag", array);
		}
		out.print(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
