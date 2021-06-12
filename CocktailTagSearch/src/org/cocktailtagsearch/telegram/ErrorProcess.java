package org.cocktailtagsearch.telegram;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ErrorProcess")
public class ErrorProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String error = "에러가 발생했습니다.";
		try {
			error +=request.getAttribute("javax.servlet.error.status_code").toString() + "\n" +
					request.getAttribute("javax.servlet.error.message").toString() + "\n" +
					request.getAttribute("javax.servlet.error.request_uri").toString() +  "\n" +
					request.getAttribute("javax.servlet.error.exception").toString() + "\n" +
					request.getAttribute("javax.servlet.error.servlet_name").toString();
		} catch(NullPointerException npe) {
		}
		Sender.send(error);
		response.sendRedirect("error.html");
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
