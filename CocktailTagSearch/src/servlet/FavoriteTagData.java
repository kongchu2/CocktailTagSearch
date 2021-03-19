package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
import Member.MemberDAO;
import Member.MemberVO;
import Tag.TagQuerier;
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
		
		JSONObject json = new JSONObject();

		HttpSession session = request.getSession(false);
		
		boolean flag = true;
		String login_id = null;
		if(session != null) {
			login_id = (String) session.getAttribute("userLogin_id");
			if(login_id == null) flag = false;
		} else {
			flag = false;
		}
		
		if(flag) {
			MemberDAO memberDAO = new MemberDAO();
			MemberVO memberVO = memberDAO.getMember(login_id);

			FavoriteTagsDAO favoriteTagsDAO = new FavoriteTagsDAO();
			int member_id = memberVO.getMember_id();
			
			TagQuerier tagDAO = new TagQuerier();
			ArrayList<TagVO> tagList = new ArrayList<TagVO>();
			
			ArrayList<Integer> loveIdList = new ArrayList<Integer>();
			
			for(int i=0;i<loveArray.size();i++) {
				int loveId = ((Long)((JSONObject)loveArray.get(i)).get("id")).intValue();
				loveIdList.add(loveId);
			}

			ArrayList<Integer> tagIdList = new ArrayList<Integer>();
			for(int i=0;i<tagArray.size();i++) {
				int tagId = ((Long)((JSONObject)tagArray.get(i)).get("id")).intValue();
				tagIdList.add(tagId);
			}
			Collections.sort(tagIdList);
			
			ArrayList<Integer> restTagIdList = favoriteTagsDAO.getFavoriteTagIdListByMember_id(member_id);
			restTagIdList.removeAll(loveIdList);
			
			if(tagIdList.size() == 0) {
				tagList = tagDAO.getTagListByTagIdList(favoriteTagsDAO.getFavoriteTagIdListByMember_id(member_id));
			} else {
				tagList = tagDAO.getTagListByTagIdListWithoutTagIdList(favoriteTagsDAO.getFavoriteTagIdListByMember_id(member_id), tagIdList);
			}
	       	JSONArray array = new JSONArray();
			JSONObject tagJson = null;
			
			for(TagVO tag : tagList) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				
				hashMap.put("id", tag.getId());
				hashMap.put("name", tag.getName());
				hashMap.put("desc", tag.getDesc());
				hashMap.put("category", tag.getCategory());
				
				tagJson = new JSONObject(hashMap);
			
				array.add(tagJson);
			}
			json.put("tag", array);
		}
		out.print(json);
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
