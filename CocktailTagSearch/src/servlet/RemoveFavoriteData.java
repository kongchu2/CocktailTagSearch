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

import FavoriteCocktail.FavoriteCocktailDAO;
import FavoriteTags.FavoriteTagsDAO;
import Member.MemberDAO;
import Member.MemberVO;

@WebServlet("/RemoveFavoriteData")
public class RemoveFavoriteData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		String cocktailStr = request.getParameter("cocktail");
		String tagStr = request.getParameter("tag");
		if(cocktailStr == null || tagStr == null) return;
		
		JSONArray cocktailArray = null;
		JSONArray tagArray = null;
		try {
			JSONParser parser = new JSONParser();
			
			if(!cocktailStr.equals("[]"))
				cocktailArray = (JSONArray)parser.parse(cocktailStr);
			if(!tagStr.equals("[]"))
				tagArray =  (JSONArray)parser.parse(tagStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ArrayList<Integer> cocktailIdList = null;
		if(!cocktailStr.equals("[]")) {
			cocktailIdList = new ArrayList<Integer>();
		
			for(int i=0;i<cocktailArray.size();i++) {
				int cocktailId = ((Long)((JSONObject)cocktailArray.get(i)).get("id")).intValue();
				cocktailIdList.add(cocktailId);
			}
		}

		ArrayList<Integer> tagIdList = null;
		if(!tagStr.equals("[]")) {
			tagIdList = new ArrayList<Integer>();
			
			for(int i=0;i<tagArray.size();i++) {
				int tagId = ((Long)((JSONObject)tagArray.get(i)).get("id")).intValue();
				tagIdList.add(tagId);
			}
		}
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
			
			int member_id = memberVO.getMember_id();
			
			FavoriteCocktailDAO FCdao = new FavoriteCocktailDAO();
			FavoriteTagsDAO FTdao = new FavoriteTagsDAO();
			
			if(cocktailIdList != null) 
				FCdao.deleteFavoriteCocktailByMember_idAndCocktail_idList(member_id, cocktailIdList);
			if(tagIdList != null)
				FTdao.deleteFavoriteTagsByMember_idAndTag_idList(member_id, tagIdList);
		}
		out.print(1);
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
