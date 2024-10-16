<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>헤더 애니메이션</title>

<script src="${root}/js/finisher-header.es5.min.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
</head>
<body style="margin: 0;">
	<div class="header finisher-header" style="width: 100%; height: 480px; display:flex; align-items: center; justify-content: center;">
		<h1 style="color: white; font-size: 100px;">DuckMusic</h1>
	</div>
	<script type="text/javascript">
		$(document).ready(
				function() {
					new FinisherHeader({
						"count" : 80,
						"size" : {
							"min" : 2,
							"max" : 7,
							"pulse" : 0.1
						},
						"speed" : {
							"x" : {
								"min" : 0,
								"max" : 0.3
							},
							"y" : {
								"min" : 0,
								"max" : 0.5
							}
						},
						"colors" : {
							"background" : "#303030",
							"particles" : [ "#1ee99a", "#c783d7", "#f1cb49",
									"#00ffef" ]
						},
						"blending" : "overlay",
						"opacity" : {
							"center" : 1,
							"edge" : 0.1
						},
						"skew" : 1,
						"shapes" : [ "c" ]
					});
				});
	</script>
</body>
</html>
