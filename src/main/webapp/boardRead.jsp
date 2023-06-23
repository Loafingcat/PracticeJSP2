<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import = "board.model.*" %>
<%@ page import = "java.util.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 글 열람</title>
</head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
  function textBox(button) {
    var textBox = $(button).siblings('.text-box');
    textBox.slideToggle();
  }
</script>
<body>
	<h3>게시판 글 열람</h3>
	<table>
		<tr>
			<td colspan="4" align="right"><a href="boardList.bbs">[목록으로]</a></td>
		</tr>
		<tr>
			<td>글 제목</td>
			<td colspan="3"><input type="text" name="subject" maxlength="50" size="50" value="${boardRead.subject}" disabled="disabled"></td>
		</tr>
		<tr>
			<td>작성자</td>
			<td><input type="text" name="name" maxlength="20" size="20" value="${boardRead.name}" disabled="disabled"></td>
			<td>조회수:${boardRead.readCnt},</td>
			<td>답글수:${boardRead.childCnt}</td>
		</tr>
		<tr>
			<td>본문</td>
			<td colspan="3"><textarea name="content" rows="8" cols="45" disabled="disabled">${boardRead.content}</textarea></td>
		</tr>
		<tr>
			<td colspan="4" align="right">
				<a href="boardUpdatePassword.bbs?num=${boardRead.num}">[수정]</a>
				<a href="boardDeletePassword.bbs?num=${boardRead.num}">[삭제]</a>
				<a href="boardReplyForm.bbs?num=${boardRead.num}">[답글]</a>
			</td>
		</tr>
	</table>
<!-- 댓글 부분 -->
<hr />
<ul>
  <c:forEach items="${getComment}" var="getComment">
    <li style="list-style:none;">
      <div id="comment" style="margin-left: ${getComment.rlev * 40}px;">
        <p>${getComment.writer} / <fmt:formatDate value="${getComment.regDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
          <a onclick="return confirm('정말로 삭제하시겠습니까?')" href="commentDelete.bbs?rno=${getComment.rno}">[X]</a>
          <c:if test="${getComment.rlev < 1}">
          <button onclick="textBox(this)">대댓글 달기</button>
          
      	  </c:if>
          <p>${getComment.content}</p>
          <div class="text-box" style="display: none;">
          <form action="replyComment.bbs" method="post">
          
          <p><label>댓글 작성자</label><input type="text" name="writer"></p>
          <input type="hidden" name="rno" value="${getComment.rno}">
          <input type="hidden" name="rref" value="${getComment.rref}">
          <input type="hidden" name="rlev" value="${getComment.rlev}">
          <input type="hidden" name="rstep" value="${getComment.rstep}">
          <textarea rows="4" cols="50" name="content"></textarea>
          <p><input type="submit" value="대댓글 달기"></p>
         
          </form>
         </div>
	  </div>
    </li>
  </c:forEach>
</ul>


<div>
	<form action="writeComment.bbs"method="post">
		<p>
			<label>댓글 작성자</label><input type="text" name="writer">
		</p>
		<p>
			<textarea rows="5" cols="50" name="content"></textarea>
		</p>
		<p>
			<input type="hidden" name="num" value="${boardRead.num}">
			<input type="submit" value="댓글 달기">
		</p>
	</form>	
</div>
</body>
</html>