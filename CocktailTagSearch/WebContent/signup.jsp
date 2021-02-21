<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session = "false"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>회원가입</title>
	<link rel="stylesheet" href="css/index.css">
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
	<div id="contents">
    <div id="menuContents">
    </div>
	<div id="postContents" style="width:40%;height:700px">
		<div id="signup">
			<h1>회원가입</h1>
			<form method="post" action="signupCtrl.jsp" name="memberInfo" onsubmit="return checkValue();">
				<input type="text" 		name="id"   	id="id"       placeholder="아이디를 입력하세요."><br>
				<input type="text" 		name="name" 	id="name"     placeholder="이름을 입력하세요."><br>
				<input type="password"	name="pw"		id="pw"       placeholder="비밀번호를 입력하세요."><br>
				<input type="password" 	name="pwCheck"	id="pwCheck"  placeholder="비밀번호를 입력하세요."><br>
				<input type="submit"	id="submit">
			</form>
		</div>
	</div>
	</div>
</body>
</html>