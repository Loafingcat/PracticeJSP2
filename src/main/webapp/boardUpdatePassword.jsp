<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 확인</title>
</head>
<body>
	<h3>게시글 수정을 위한 비밀번호 확인</h3>
	<form action="boardUpdateCheck.bbs" method="post">
		<input type="password" name="password">
		<input type="hidden" name="num" value="${num}">
		<input type="submit" value="입력">
	</form>
</body>
</html>