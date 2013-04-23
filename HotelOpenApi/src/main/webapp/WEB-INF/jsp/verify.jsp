<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=2.0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快捷酒店</title>
<style type="text/css">
body{ background:#ebeff2; font-family:Helvetica; padding-bottom:22px;}
*{ margin:0; padding:0; list-style:none;}
.tips{ background:#586066; color:#FFF; font-size:15px; padding:8px 0 8px 0;}
.tips .tips_con{ line-height:17px; margin:4px 0 0 15px;}
.tips p{ margin:0 0 0 15px; font-size:13px; color:#a7abad;}
p.tips_p{ color:#7a7f85; margin:20px 0 0 15px; font-size:16px; text-shadow:#FFF 1px 1px; width:90.6%; font-weight:bold;}
.phone_input{margin:8px 0 0 15px;  height:30px; line-height:32px;font-size:15px; -webkit-appearance:none; width:90.6%; border:1px solid #bcc3cc; border-right:1px solid #ddd; border-bottom:1px solid #DDD; height:32px; line-height:30px; border-radius:1px;}
.submit_div{ margin-top:24px; text-align:center; clear:both;}
.submit_div input{ background:#f48e3c;height:45px;width:90.6%; border-radius:4px; line-height:45px; text-align:center; color:#FFF; font-size:20px; -webkit-appearance:none;  border:none; font-weight:bold;}
</style>
</head>
<body>
<div class="kj">
	<div class="tips">
		<p>您将预订如下酒店：</p>
        <div class="tips_con">${hotelBrand}-${hotelName }<br>地址：${add}</div>
    </div>
		<form action="commit" method="get">
			<p class="tips_p">填写手机验证码完成预订</p>
            <input type="text" name="verify" size="14" class="phone_input">
            
			<div>
				<input type="hidden" name="kid" value="${kid}">
				<input type="hidden" name="hotelType" value="${hotelType}">
				<input type="hidden" name="hotelId" value="${hotelId}">
				<input type="hidden" name="test" value="test">
			</div>
			<div class="submit_div"><input type="submit" value="提交"></div>
		</form>

</div>
</body>
</html>