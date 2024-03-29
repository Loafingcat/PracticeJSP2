<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 새글 쓰기</title>
	<script type="text/javascript">
			function checkForm() {
				if (document.write.password.value.length < 4 || document.write.password.value.length > 8) {
					alert("비밀번호를 4~8자 이내로 입력해주세요.");
					return false;
				}
			}
	</script>
</head>
<body>
	<h3>게시판 새글 쓰기</h3>
	<form name="write" action="boardWrite.bbs"method="post">
		<table>
			<tr>
				<td colspan="4" align="right"><a href="boardList.bbs">
				[목록으로]</a></td>
			</tr>
			<tr>
				<td>글 제목</td>
				<td colspan="3"><input type="text" name="subject" maxlength="50" size="50"></td>
			</tr>
			<tr>
				<td>작성자</td>
				<td><input type="text" name="name" maxlength="20" size="20">
				</td>
				<td>비밀번호</td>
				<td><input type="password" name="password" maxlength="20" size="12">
				</td>
			</tr>
			<tr>
				<td>본문</td>
				<td colspan="3"><textarea name="content" rows="8" cols="45"></textarea></td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					<input type="submit" value="글 올리기" onclick="return checkForm()">
				</td>
			</tr>
			
		</table>
	</form>
</body>
</html>