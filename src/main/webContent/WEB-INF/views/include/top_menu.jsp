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
	<!-- Navigation-->
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container px-4 px-lg-5">
			<a class="navbar-brand" href="${root }main">Start Bootstrap</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
				aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="#!">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="#!">About</a></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" id="navbarDropdown" href="#"
						role="button" data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
						<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
							<li><a class="dropdown-item" href="#!">All Products</a></li>
							<li><hr class="dropdown-divider" /></li>
							<li><a class="dropdown-item" href="#!">Popular Items</a></li>
							<li><a class="dropdown-item" href="#!">New Arrivals</a></li>
						</ul></li>
				</ul>

				<ul class="navbar-nav ml-auto">
					<c:choose>
						<c:when test="${loginMemberBean.memberLogin == true }">
							<!-- 로그인시 -->
							<li class="nav-item"><a href="${root }member/modify"
								class="nav-link">정보수정</a></li>
							<li class="nav-item"><a href="${root }member/logout"
								class="nav-link">로그아웃</a></li>
							<form:form class="d-flex">
								<form:button class="btn btn-outline-dark" type="submit">
									<i class="bi-cart-fill me-1"></i>
                            Cart
                            <span
										class="badge bg-dark text-white ms-1 rounded-pill">0</span>
								</form:button>
							</form:form>
						</c:when>
						<c:otherwise>
							<!-- 로그아웃시 -->
							<li class="nav-item"><a href="${root }member/login"
								class="nav-link">로그인</a></li>
							<li class="nav-item"><a href="${root }member/join"
								class="nav-link">회원가입</a></li>
						</c:otherwise>
					</c:choose>
				</ul>


			</div>
		</div>
	</nav>
</body>
</html>