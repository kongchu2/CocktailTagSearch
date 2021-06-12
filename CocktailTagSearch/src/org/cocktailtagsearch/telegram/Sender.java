package org.cocktailtagsearch.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sender {	
	public static void send(String text) {
		BufferedReader in = null;
		String url = "https://api.telegram.org/bot" + PrivateData.token + "/sendmessage?chat_id=" + PrivateData.chat_id + "&text=" + text;
		 try {
			 URL obj = new URL(url);
			 
			 HttpURLConnection con = (HttpURLConnection)obj.openConnection();
			 con.setRequestMethod("GET");
			 in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			 String line;
			 
			 while((line = in.readLine()) != null) {
				 System.out.println(line);
			 }
			 
		 } catch(Exception e) {
			 e.printStackTrace();
			 System.out.println(url);
		 } finally {
			 if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
		 }
	}
}
