<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var='root' value="${pageContext.request.contextPath }/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>메인페이지입니다.</h1>

	<h2>
		<a href="${root }temp/tempMain">To tempMain</a>
	</h2>
	<br />
	<hr />
	<br />
		<h1>퀴즈페이지입니다.</h1>
	<h2>
		<a href="${root }quiz/quizMain">To quizMain</a>
	</h2>
</body>
</html>