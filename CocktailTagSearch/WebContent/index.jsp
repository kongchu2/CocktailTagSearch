
<%@page import="Cocktail.CocktailVO"%>
<%@page import="Cocktail.CocktailDAO"%>
<%@page import="Tag.TagVO"%>
<%@page import="Tag.TagDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>칵테일</title>
	<link rel="stylesheet" href="css/index.css">
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
</head>
<body>
	<div id="contents">
	    <div id="menuContents">
	        
	    </div>
	    <div id="searchContents">
	        <form method="get" target="_blank" id="search">
				<img src="image/search.png" alt="search" class="searchIcon image" />
				<input type="search"
			           id="searchText"
			           autocomplete="off"
			           spellcheck="false"
			           aria-live="polite"/>
	          	<img src="image/cancel.png" alt="cancel" class="cancelIcon image" />
	        </form>
	        <div id="autocompleteTagsContents">
	        	<%
	        	TagDAO tagDAO = new TagDAO();
	        	ArrayList<TagVO> tagList = tagDAO.getTagList();  
	        	for(TagVO tag : tagList) {
					out.print("<div class=\"autocompleteTags\">");
						out.print(tag.getName());
					out.print("</div>");
				}
	        	%>
        	</div>
       		<div id="searchTagsContents">
        	</div>
	    </div>
	    <main>
	      	<div id="cocktailContents">
		        <div id="cocktailMenuContents"></div>
		        <%
		        CocktailDAO cocktailDAO = new CocktailDAO();
		        ArrayList<CocktailVO> cocktailList = cocktailDAO.getCocktailList();
		        		        		        		        	
		        for(CocktailVO cocktail : cocktailList) {
		        	out.print("<div class=\"cocktailItems\">");
		        %>
				        <img src=<%=cocktail.getImage() %> class='cocktailImages'>
				        <div class='itemTitle'><%=cocktail.getName()%></div>
					    <div class="itemTagsBox">
					        <%
					        for(TagVO tag : cocktail.getTagList()) {
					        	out.print("<div class=\"itemTags\">");
					        	out.print(tag.getName());
					        	out.print("</div>");
					        } 
					        %>
				        </div>	
			    <%
			    	out.print("</div>");
			    }
			    %>
        	<div class="cocktailNotFound">NOT FOUND</div>
	   		</div>
	      	<div id="tagContents">
	          
	      	</div>
	    </main>
	</div>
   	<script src="js/index.js"></script>
</body>
</html>