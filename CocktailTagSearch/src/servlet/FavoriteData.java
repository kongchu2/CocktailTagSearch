package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import Cocktail.CocktailDAO;
import Cocktail.CocktailVO;
import FavoriteCocktail.FavoriteCocktailDAO;
import FavoriteCocktail.FavoriteCocktailVO;
import FavoriteTags.FavoriteTagsDAO;
import FavoriteTags.FavoriteTagsVO;
import Member.MemberDAO;
import Member.MemberVO;
import Tag.TagDAO;
import Tag.TagVO;

@WebServlet("/FavoriteData")
public class FavoriteData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		HttpSession session = request.getSession(false);
		
		MemberDAO memberDAO = null;
		MemberVO memberVO = null;
		FavoriteCocktailDAO favoriteCocktailDAO = null;
		FavoriteCocktailVO favoriteCocktailVO = null;
		FavoriteTagsDAO favoriteTagsDAO = null;
		FavoriteTagsVO favoriteTagsVO = null;
		CocktailDAO cocktailDAO = null;
		CocktailVO cocktailVO = null;
		TagDAO tagDAO = null;
		TagVO tagVO = null;
		
		ArrayList<FavoriteCocktailVO> favoriteCocktailList = null;
		ArrayList<FavoriteTagsVO> favoriteTagList= null;
		ArrayList<CocktailVO> cocktailList = null;
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

			JSONObject userJson = new JSONObject();
			
			userJson.put("name", memberVO.getName());
			userJson.put("login_id", memberVO.getLogin_id());
			userJson.put("member_id", memberVO.getMember_id());
			
			favoriteCocktailDAO = new FavoriteCocktailDAO();
			favoriteCocktailList = favoriteCocktailDAO.getFavoriteCocktailListByMember_id(memberVO.getMember_id());

			favoriteTagsDAO = new FavoriteTagsDAO();
			favoriteTagList = favoriteTagsDAO.getFavoriteTagListByMember_id(memberVO.getMember_id());
			
			cocktailDAO = new CocktailDAO();
			cocktailList = new ArrayList<CocktailVO>();
	       	for(FavoriteCocktailVO favoriteCocktail : favoriteCocktailList) {
	       		cocktailList.add(cocktailDAO.getCocktail(favoriteCocktail.getCocktail_id()));
	       	}
	       	JSONArray cocktailArray = new JSONArray();
			JSONObject cocktailJson = null;
	       	for(CocktailVO cocktail : cocktailList) {
	       		cocktailJson = new JSONObject();
	       		
	       		cocktailJson.put("id", cocktail.getId());
	       		cocktailJson.put("name", cocktail.getName());
	       		cocktailJson.put("desc", cocktail.getDesc());
	       		
	       		cocktailArray.add(cocktailJson);
	       	}
	       	
	       	tagDAO = new TagDAO();
			tagList = new ArrayList<TagVO>();
	       	for(FavoriteTagsVO favoriteTag : favoriteTagList) {
	       		tagList.add(tagDAO.getTag(favoriteTag.getTag_id()));
	       	}
	       	JSONArray tagArray = new JSONArray();
			JSONObject tagJson = null;
			for(TagVO tag : tagList) {
				tagJson = new JSONObject();
				
				tagJson.put("id", tag.getId());
				tagJson.put("name", tag.getName());
				tagJson.put("desc", tag.getDesc());
				tagJson.put("category", tag.getCategory());
			
				tagArray.add(tagJson);
			}

			json.put("user", userJson);
			json.put("cocktail", cocktailArray);
			json.put("tag", tagArray);
		}
		out.print(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
