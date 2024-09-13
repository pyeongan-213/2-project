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
<script>
	function checkMemberIdExist(){
		var member_id = $("#member_id").val()
		
		if(member_id.length == 0){
			alert('아이디를 입력해주세요')
			 return
		}
		
		$.ajax({
			url : '${root}member/checkMemberIdExist/' + member_id,
			type : 'get',
			dataType : 'text',
			success : function(result){
				if(result.trim() == 'true'){
					alert('사용할 수 있는 아이디 입니다')
					$("#memberIdExist").val('true')
				}else if(result.trim() == 'false'){
					alert('사용할 수 없는 아이디 입니다')
					$("#memberIdExist").val('false')
				}
			}
		})		
	}
	function resetMemberIdExist(){
		$("#memberIdExist").val('false')
	}	
	
</script>
<body>

<c:import url="/WEB-INF/views/include/top_menu.jsp"/>

<div class="container" style="margin-top:100px">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<div class="card shadow">
				<div class="card-body">
					<form:form action="${root }member/join_pro" method="post" modelAttribute="joinMemberBean">
						<form:hidden path="memberIdExist"/>
						<div class="form-group">
							<form:label path="membername">이름</form:label>
							<form:input path="membername" class="form-control"/>
							<form:errors path="membername" style='color:red'/>
						</div>
						<div class="form-group">
							<form:label path="member_id">아이디</form:label>
							<div class="input-group">
								<form:input path="member_id" class='form-control' onkeypress="resetMemberIdExist()"/>
								<div class="input-group-append">
									<button type="button" class="btn btn-primary" onclick='checkMemberIdExist()'>중복확인</button> 
								</div>
							</div>
							<form:errors path="member_id" style='color:red'/>
						</div>
						<div class="form-group">
							<form:label path="password">비밀번호</form:label>
							<form:password path="password" class="form-control"/>
							<form:errors path="password" style='color:red'/>
						</div>
						<div class="form-group">
							<form:label path="password2">비밀번호 확인</form:label>
							<form:password path="password2" class="form-control"/>
							<form:errors path="password2" style='color:red'/>
						</div>
						<div class="form-group">
							<div class="text-right">
								<form:button type="submit" class="btn btn-primary">회원가입</form:button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom_info.jsp"/>

</body>
</html>