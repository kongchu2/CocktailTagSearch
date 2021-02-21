
<%@page import="Tag.TagVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Tag.TagDAO"%>
<%@page import="Cocktail.CocktailVO"%>
<%@page import="Cocktail.CocktailDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session = "false"%>
<%
request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>포스트</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
	<%
	int postId = Integer.parseInt(request.getParameter("id"));
			
			CocktailDAO gc = new CocktailDAO();
			CocktailVO cocktail = gc.getCocktail(postId);
			
			TagDAO dao = new TagDAO();
			ArrayList<TagVO> tagList = dao.getTagListByCocktailId(postId);
	%>
    <div id="contents">
        <div id="menuContents">
        	<a href="index.html"><div id="title">칵테일 태그검색</div></a>
        </div>
        <div id="postContents">
            <div id="postTitleContents">
                <div class="postTitle"><%=cocktail.getName()%></div>
            </div>
            <img src="image/notFilledHeart.png" id="likeimg">
            <div id="cocktailImageContents">
                <img src=<%=cocktail.getImage()%> alt="칵테일사진" class="cocktailImages">
            </div>
            <div id="explanationContents">
                <div class="description">
                    <!-- 간단 설명 -->
                    <%=cocktail.getDesc()%>	
                </div>	
                <br>
                <div class="history">
                    <!-- 역사 -->
                    <%=cocktail.getHistory()%>
                </div>
                <br>
                <div class="taste">
                    <!-- 맛 -->
                    <%=cocktail.getTaste()%>
                </div>
                <br>
                <div class="base">
                    <!-- 베이스 -->
                    <%=cocktail.getBase()%>
                </div>
                <br>
                <div class="build">
                    <!-- 조주 방법 -->
                    <%=cocktail.getBuild()%>
                </div>
                <br>
                <div class="glass">
                    <!-- 잔 -->
                    <%=cocktail.getGlass()%>
                </div>
                
            </div>
            <div id="postTagContents">
            	<%
            	for(TagVO tag : tagList) {
            		out.print("<a desc="+ tag.getDesc().replaceAll(" ", "&nbsp;") +" href=javascript:likeTag("+tag.getId()+")><div class=\"tagBlock "+ tag.getCategory()+"\">" + tag.getName() + "</div></a>");
            	}
            	%>
                <!-- <div class="tagBlock base">소주</div>
                <div class="tagBlock material">에스프레소</div>
                <div class="tagBlock material">커피</div>
                <div class="tagBlock taste">쓴맛</div>
                <div class="tagBlock glass">커피잔</div>
                <div class="tagBlock glass">소주병</div> -->
            </div>
        </div>
		<footer>
        </footer>
    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="js/post.js"></script>
    <script>var postId=<%=postId%></script>
</body>
</html>
