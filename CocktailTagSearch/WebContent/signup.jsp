<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>회원가입</title>
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
	<form method="post" action="signupCtrl.jsp" name="memberInfo" onsubmit="return checkValue();">
		아이디<input type="text" 		name="id"   	id="id"><br>
		이름<input type="text" 		name="name" 	id="name"><br>
		비밀번호<input type="password"	name="pw"		id="pw"><br>
		비밀번호 확인<input type="password" 	name="pwCheck"	id="pwCheck"><br>
		<input type="submit"	id="submit">
	</form>
</body>
</html>