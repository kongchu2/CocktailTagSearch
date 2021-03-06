<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session = "false"%>
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style type="text/css">
    </style>
    <link rel="stylesheet" href="css/index.css">
    <script src="./js/jquery.js"></script>
    <script>   
		function checkValue() {
			if(document.memberInfo.id.value == "") {
				alert("아이디를 입력하세요");
				return false;
			}
			if(document.memberInfo.name.value == "") {
				alert("이름을 입력하세요");
				return false;
			}
			if(document.memberInfo.pw.value == "") {
				alert("비밀번호를 입력하세요");
				return false;
			}
			if(document.memberInfo.pw.value != document.memberInfo.pwCheck.value) {
				alert("비밀번호 확인과 비밀번호가 동일하지 않습니다.");
				return false;
			}
		}
	</script>
</head>
<body>
   <div id="signin-wrap">
   		<div id="signinBannerContents">
   			<div id="title" class="signinBannerTitle">
   				<p class="mainTitle" />
   				<p class="subTitle" />
   			</div>
   			<div class="signin-banner-container">
   				<div>
   					<img src="./image/icon/signin_banner.png" />
   				</div>
   			</div>
		</div>
		<div id="signinContents">
			<span class="signin-signup-linked"> Already a member? <a href="./login.html">Sign in</a> </span>
			<div class="signin-inner-container">
   				<div id="title"><p class="mainTitle" /></div>
				<div class="signinTitle"> <h1>Sign in to Cocktail Tags</h1> </div>
				<form method="post" action="signupCtrl.jsp" name="memberInfo" onsubmit="return checkValue();">
					<div class="signin-input">
						<span>UserID</span>
						<input type="text" id="id" class="inputBox" name="id">
					</div>
					<div class="signin-input">
						<span>Username</span>
						<input type="text" id="name" class="inputBox" name="name">
					</div>
					<div class="signin-input">
						<span>Password</span>
						<input type="password" id="pw" class="inputBox" name="pw">
					</div>
					<div class="signin-input">
						<span>Re-enter password</span>
						<input type="password" id="pwCheck" class="inputBox" name="pwCheck">
					</div>
					<div class="signin-button-inner-container">
						<div class="signin-button">
							<input type="submit" value="Create Account" class="submitBtn" onclick="login()">
						</div>
					</div>
				</form>
			</div>
		</div>
   </div>
   <script src="js/loadMenu.js"></script>
</body>
</html>