package org.cocktailtagsearch.tag.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.tag.TagQuerier;
import org.cocktailtagsearch.tag.TagVO;
import org.json.simple.JSONObject;


@WebServlet("/AddTag")
public class AddTag extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");
		String category = request.getParameter("category");
		
		TagVO vo = new TagVO();
		vo.setName(name);
		vo.setDesc(desc);
		vo.setCategory(category);
		
		TagQuerier dao = new TagQuerier();
		int count = dao.InsertTag(vo);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("isAdded", count>0 ? "1":"0");
		JSONObject json = new JSONObject(map);
		out.print(json);
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
