<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=2.0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快捷酒店</title>
<style type="text/css">
body {
	background: #ebeff2;
	font-family: Helvetica;
	padding-bottom: 15px;
}

* {
	margin: 0;
	padding: 0;
	list-style: none;
}

.kj {
	text-align: center;
}

.tips {
	font-size: 15px;
	background: #579ed9;
	float: left;
	width: 100%;
	display: block;
}

.tips h2 {
	background:url(../images/smile.png) no-repeat 10px center;
	background-size: 50px 50px;
	padding-left: 70px;
	color: #FFF;
	display: inline-block;
	height: 120px;
	line-height: 120px;
	float: left;
	margin-left: 25px;
}

.tips_1,tips_p {
	width: 90.6%;
	float: left;
}

.tips_1 {
	margin: 20px 0 0 15px;
	line-height: 22px;
	font-size: 15px;
}

p.tips_p {
	color: #7a7f85;
	margin: 45px 0 0 15px;
	text-shadow: #FFF 1px 1px;
	font-size: 14px;
	display: inline-block;
	width: 90.6%;
}

a {
	background: #f48e3c;
	width: 90.6%;
	height: 45px;
	border-radius: 4px;
	line-height: 45px;
	text-align: center;
	color: #FFF;
	font-size: 20px;
	display: inline-block;
	text-decoration: none;
	margin-top: 10px;
}
</style>
</head>

<body>
	<div class="kj">
		<c:choose>

			<c:when test="${status=='00'}">
				<div class="tips">
					<h2>恭喜您，预订成功！</h2>
				</div>
				<div class="tips_1">
					<p>稍后您将会收到酒店发给你的确认短信。</p>
					<p>如果您无法按规定时间入住，请联系酒店。</p>
				</div>
				<p class="tips_p">
					享受更多快捷酒店的会员预订，<br>下载快捷酒店管家！
				</p>
				<a href="http://h.133.cn">了解更多</a>
			</c:when>

			<c:otherwise>  
   ${msg}
   </c:otherwise>

		</c:choose>
	</div>

</body>
</html>