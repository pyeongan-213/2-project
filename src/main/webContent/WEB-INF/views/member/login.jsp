<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value="${pageContext.request.contextPath }/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>로그인페이지</h1>
	<form:form action="${root }member/login_pro" method='post'>
		<div class="form-group">
			<form:label path="member_id">아이디</form:label>
			<form:input path="member_id" class="form-control" />
			<form:errors path="member_id" style="color:red" />
		</div>
		<div class="form-group">
			<form:label path="password">비밀번호</form:label>
			<form:password path="password" class="form-control" />
			<form:errors path="password" style="color:red" />
		</div>
		<div class="form-group text-right">
			<form:button class='btn btn-primary'>로그인</form:button>
			<a href="${root }member/join" class="btn btn-danger">회원가입</a>
		</div>
	</form:form>
</body>
</html>