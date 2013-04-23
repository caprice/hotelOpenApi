<%@ page language="java" pageEncoding="utf-8"%>
<%@ page  contentType="text/html; charset=utf-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=2.0" />
<title>快捷酒店</title>
<style type="text/css">
body{ background:#ebeff2; font-family:Helvetica; padding-bottom:22px;}
*{ margin:0; padding:0; list-style:none;}
.tips{ background:#586066; color:#FFF; font-size:15px; padding:8px 0 8px 0;}
.tips .tips_con{ line-height:17px; margin:4px 0 0 15px;}
.tips p{ margin:0 0 0 15px; font-size:13px; color:#a7abad;}
p.tips_p{ color:#7a7f85; margin:10px 0 0 15px; font-size:16px; text-shadow:#FFF 1px 1px; width:90.6%; font-weight:bold;}
.bd_list{margin:3px 0 0 15px; display:inline-block;width:90.6%; }
.bd_list li{ margin-top:5px; float:left;width:100%;}
.bd_list li label{width:65px; font-size:15px; text-align:right;display:inline-block; float:left; margin-top:6px;}
.bd_list li input, .bd_list li select{ float:left;font-size:15px; margin-left:5px; width:211px; -webkit-appearance:none; border:1px solid #bcc3cc; border-right:1px solid #ddd; border-bottom:1px solid #DDD; height:32px; line-height:30px; padding-left:3px; border-radius:1px;}
.bd_list li input{ color:#b3b3b3; font-size:13px;}
.bd_list li select{ width:216px!important; margin-top:1px; background:url(<%=request.getContextPath()%>/images/r_dot.png) no-repeat 200px center #FFF; background-size:9px 13px;}
.bd_list li span{ margin-left:5px}
.submit_div{ margin-top:2px; text-align:center; clear:both;}
.submit_div input{ background:#f48e3c;height:45px;width:90.6%; border-radius:4px; line-height:45px; text-align:center; color:#FFF; font-size:20px; -webkit-appearance:none;  border:none; font-weight:bold;}
.sty{ margin-top:0px!important;}
</style>

</head>
<body>
<div class="kj">
	<div class="tips">
		<p>您将预订如下酒店：</p>
        <div class="tips_con">${hotelRoom.hotelBrand}--${hotelRoom.hotelName }<br>地址：${hotelRoom.add}</div>
    </div>
    <form action="preprocess" method="post">
        <p class="tips_p">填写入住信息，完成预订</p>
        <ul class="bd_list">
            <li><label class="sty">入住时间:</label><span>${beginDate}-${endDate}</span></li>
            <li>
                <label>入住房型:</label>
                <select name="roomInfo">
						<c:forEach items="${hotelRoom.rooms}" var="room" varStatus="vs">
							<option value="${room.typeid },${room.type},${room.memberPrice}"
								${vs.last ? "selected='selected'":""}>${room.type
								}&nbsp;&nbsp;¥${room.memberPrice}</option>
							<br />
						</c:forEach>
				</select>
            </li>
            <li><label>入住人:</label>
            <input type="text" name="name" size="20" value="${nick==null?'请输入您的姓名':nick }" onblur="if(!this.value) {this.value='请输入您的姓名';this.style.color='#b2b2b2';}" onfocus="if(this.value=='请输入您的姓名') this.value='';this.style.color='#000000'"></li>
            <li><label>身份证:</label>
            <input type="text" name="idCard" size="20" value="${idcard==null?'身份证号，用来向酒店提交订单':idcard }" onblur="if(!this.value) {this.value='身份证号，用来向酒店提交订单';this.style.color='#b2b2b2';}" onfocus="if(this.value=='身份证号，用来向酒店提交订单') this.value='';this.style.color='#000000'"></li>
            <li><label>手机号:</label><input type="text" name="mobile" size="20"  value="${tel==null?'手机号，用来确认订单':tel }" onblur="if(!this.value) {this.value='手机号，用来确认订单';this.style.color='#b2b2b2';}" onfocus="if(this.value=='手机号，用来确认订单') this.value='';this.style.color='#000000'"></li>
            <li><input type="hidden" name="checkinDate" value="${hotelRoom.startDate }"></li>
            <li><input type="hidden" name="checkoutDate" value="${hotelRoom.endDate }"></li>
            <li><input type="hidden" name="hotelId" value="${hotelRoom.hotelId }"></li>
            <li><input type="hidden" name="hotelType" value="${hotelRoom.hotelType }"></li>
            <li><input type="hidden" name="test" value="test"></li>
        </ul>
        <div class="submit_div"><input type="submit" value="提交订单"></div>
    </form>
</div>
</body>
</html>