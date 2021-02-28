package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import Cocktail.CocktailDAO;

@WebServlet("/DeleteCocktail")
public class DeleteCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		int cocktailId = Integer.parseInt(request.getParameter("cocktailId"));
		
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(false);
		JSONObject json = new JSONObject();
		if((char)session.getAttribute("permission") == '1') {
			CocktailDAO dao = new CocktailDAO();
			if(dao.DeleteCocktail(cocktailId) > 0) {
				json.put("isDeleted", "1");
			} else {
				json.put("isDeleted", "0");
			}
		}
		
		out.print(json);
		
		
}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
