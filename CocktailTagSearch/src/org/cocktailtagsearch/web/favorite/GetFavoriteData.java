package org.cocktailtagsearch.web.favorite;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cocktailtagsearch.cocktail.CocktailQuerier;
import org.cocktailtagsearch.cocktail.CocktailVO;
import org.cocktailtagsearch.favoritecocktail.FavoriteCocktailQuerier;
import org.cocktailtagsearch.favoritecocktail.FavoriteCocktailVO;
import org.cocktailtagsearch.favoritetag.FavoriteTagsQuerier;
import org.cocktailtagsearch.favoritetag.FavoriteTagsVO;
import org.cocktailtagsearch.member.MemberQuerier;
import org.cocktailtagsearch.member.MemberVO;
import org.cocktailtagsearch.tag.TagQuerier;
import org.cocktailtagsearch.tag.TagVO;
import org.json.simple.JSONObject;

@WebServlet("/GetFavoriteData")
public class GetFavoriteData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		HashMap<String, Object> json = new HashMap<String, Object>();

		HttpSession session = request.getSession(false);
		
		MemberQuerier memberDAO = null;
		MemberVO memberVO = null;
		FavoriteCocktailQuerier favoriteCocktailDAO = null;
		FavoriteTagsQuerier favoriteTagsDAO = null;
		CocktailQuerier cocktailDAO = null;
		TagQuerier tagDAO = null;
		
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

		HashMap<String, Object> map = new HashMap<String, Object>();
		if(flag) {
			memberDAO = new MemberQuerier();
			memberVO = memberDAO.getMember(login_id);

			HashMap<String, Object> userMap = new HashMap<String, Object>();
			
			userMap.put("name", memberVO.getName());
			userMap.put("login_id", memberVO.getLogin_id());
			userMap.put("member_id", memberVO.getMember_id());
			
			favoriteCocktailDAO = new FavoriteCocktailQuerier();
			favoriteCocktailList = favoriteCocktailDAO.getFavoriteCocktailListByMember_id(memberVO.getMember_id());

			favoriteTagsDAO = new FavoriteTagsQuerier();
			favoriteTagList = favoriteTagsDAO.getFavoriteTagListByMember_id(memberVO.getMember_id());
			
			cocktailDAO = new CocktailQuerier();
			cocktailList = new ArrayList<CocktailVO>();
	       	for(FavoriteCocktailVO favoriteCocktail : favoriteCocktailList) {
	       		cocktailList.add(cocktailDAO.getCocktail(favoriteCocktail.getCocktail_id()));
	       	}
	       	ArrayList<HashMap<String, Object>> cocktailArray = new ArrayList<HashMap<String,Object>>();
	       	for(CocktailVO cocktail : cocktailList) {
	       		HashMap<String, Object> cocktailJson = new HashMap<String, Object>();
	       		
	       		cocktailJson.put("id", cocktail.getId());
	       		cocktailJson.put("name", cocktail.getName());
	       		cocktailJson.put("desc", cocktail.getDesc());
	       		
	       		cocktailArray.add(cocktailJson);
	       	}
	       	
	       	tagDAO = new TagQuerier();
			tagList = new ArrayList<TagVO>();
	       	for(FavoriteTagsVO favoriteTag : favoriteTagList) {
	       		tagList.add(tagDAO.getTag(favoriteTag.getTag_id()));
	       	}
	       	ArrayList<HashMap<String, Object>> tagArray = new ArrayList<HashMap<String,Object>>();
			for(TagVO tag : tagList) {
				HashMap<String, Object> tagJson = new HashMap<String, Object>();
				
				tagJson.put("id", tag.getId());
				tagJson.put("name", tag.getName());
				tagJson.put("desc", tag.getDesc());
				tagJson.put("category", tag.getCategory());
			
				tagArray.add(tagJson);
			}

			json.put("user", userMap);
			json.put("cocktail", cocktailArray);
			json.put("tag", tagArray);
			map.put("favorite", json);
		}
		out.print(new JSONObject(map));
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
