
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
<<<<<<< HEAD
=======
    <style>
        .border {
            border: 1px solid black;
        }
        #content {
            width:550px;
        }
        #search {
            vertical-align: top;
            width:200px;
        }
        input{
            width:100%;
            padding:0px;
            border:none;
        }
        .editAutocompleteTag, .addedTag {
            border: 1px solid black;
        }
        #addedTagList {
            vertical-align: top;
            width:300px;
        }
        .inline-block {
            display: inline-block;
        }
        .deleteBtn {
            float:right;
            margin-right:5px;
        }
        .postAdminBtn {
            margin: 15px;
        }
        a {
            text-decoration: none;
        }
    </style>
>>>>>>> refs/remotes/origin/main
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
        </div>
        <div id="post-wrap">
        <div id="postContents">
            <div id="postTitleContents">
                <div class="postTitle"><%=cocktail.getName()%></div>
            </div>
            <div id="postAdminBtnContents">
            </div>
            <img src="image/icon/notFilledHeart.png" id="likeimg">
            <div id="cocktailImageContents">
            	<div class = "post-image-container">
            		<img src=<%=cocktail.getImage()%> alt="칵테일사진" class="cocktailImages">
            	</div>           
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
            		out.print("<a desc="+ tag.getDesc().replaceAll(" ", "&nbsp;") +" href=javascript:likeTag("+tag.getId()+")><div tagId=\""+tag.getId()+"\" class=\"tagBlock "+ tag.getCategory()+"\">" + tag.getName() + "</div></a>");
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
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script>var postId=<%=postId%></script>
    <script src="js/loadMenu.js"></script>
    <script src="js/post.js"></script>
</body>
</html>
