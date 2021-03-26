	package org.cocktailtagsearch.web.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cocktailtagsearch.util.security.RsaDecryption;
import org.json.simple.JSONObject;

@WebServlet("/GetRsaKey")
public class GetRsaKey extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private void createKey() {
		KeyPairGenerator generator;
		
		try {
			generator = KeyPairGenerator.getInstance(RsaDecryption.RSA_INSTANCE);
		    SecureRandom random = new SecureRandom();
			generator.initialize(1024, random);
		
		    KeyPair keyPair = generator.genKeyPair();
		    KeyFactory keyFactory = KeyFactory.getInstance(RsaDecryption.RSA_INSTANCE);
		    RsaDecryption.PUBLIC_KEY = keyPair.getPublic();
		    RsaDecryption.SESSION_KEY = keyPair.getPrivate();
		    RsaDecryption.publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(RsaDecryption.PUBLIC_KEY, RSAPublicKeySpec.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		createKey();
	
		HashMap<String, Object> map = new HashMap<String, Object>();
	  try {
	      String publicKeyModulus = RsaDecryption.publicSpec.getModulus().toString(16);
          String publicKeyExponent = RsaDecryption.publicSpec.getPublicExponent().toString(16);
	      map.put("RSAModulus", publicKeyModulus);
	      map.put("RSAExponent", publicKeyExponent);
	  } catch (Exception e) {
	  	e.printStackTrace();
	  }
	  System.out.println(map);
	  	out.print(new JSONObject(map));
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
