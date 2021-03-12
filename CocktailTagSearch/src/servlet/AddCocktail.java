package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Cocktail.CocktailDAO;
import Cocktail.CocktailVO;

@WebServlet("/AddCocktail")
public class AddCocktail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONParser jsonParser = new JSONParser();
		CocktailVO cocktail = new CocktailVO();
		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			if (part.getHeader("Content-Disposition").contains("filename=")) {
				String fileName = extractFileName(part.getHeader("Content-Disposition"));
				if (part.getSize() > 0) {
					part.write(getProjectPath() + File.separator + "image" + File.separator + "cocktail"
							+ File.separator + fileName);
					cocktail.setImage(fileName);
					part.delete();
				}
			}
		}
		cocktail.setName(request.getParameter("name"));
		cocktail.setDesc(request.getParameter("desc"));
		cocktail.setHistory(request.getParameter("history_desc"));
		cocktail.setTaste(request.getParameter("taste"));
		cocktail.setBase(request.getParameter("base_alcohol"));
		cocktail.setBuild(request.getParameter("build_method"));
		cocktail.setGlass(request.getParameter("cocktail_glass"));
		JSONArray tagJsonArray = null;
		ArrayList<Integer> tagIdList = new ArrayList<Integer>();
		try {
			tagJsonArray = (JSONArray)jsonParser.parse(request.getParameter("tags"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(int i=0;i<tagJsonArray.size();i++) {
			tagIdList.add(Integer.parseInt(tagJsonArray.get(i).toString()));
		}		
		CocktailDAO dao = new CocktailDAO();
		int count = dao.InsertCocktail(cocktail, tagIdList);
		JSONObject json = new JSONObject();
		json.put("isAdded", count>0 ? "1":"0");
		out.print(json);
	}

	private String extractFileName(String partHeader) {
		for (String cd : partHeader.split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
				int index = fileName.lastIndexOf(File.separator);
				return fileName.substring(index + 1);
			}
		}
		return null;
	}

	private String getProjectPath() {
		String url = this.getClass().getResource("").getPath();
		String path = url.substring(1, url.indexOf(".metadata")) + "CocktailTagSearch/WebContent";
		return path;
	}
}
