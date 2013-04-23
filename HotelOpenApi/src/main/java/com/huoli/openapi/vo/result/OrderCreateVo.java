package com.huoli.openapi.vo.result;

import org.apache.commons.lang.StringUtils;

import com.huoli.openapi.vo.data.OrderVerify;

public class OrderCreateVo {
	//{"arrdate":"2012-11-12","depdate":"2012-11-13","rmPrice":"79",
		//	"hotelId":"011021034","persons":1,"rateCode":"NOR",
		//	"roomDesc":"大床房B","email":"dhj@ghj.com","hotelName":"99旅馆-上海九亭店",
			//"rmType":"大床房B","roomNum":1,"gstName":"张扬","totalPrice":"79"
			//	,"mobile":"18064088729"}
	private String arrdate;
	private String depdate;
	private String rmPrice;
	private String hotelId;
	private String roomDesc;
	private String rateCode;
	private String email;
	private String hotelName;
	private String rmType;
	private String gstName;
	private String totalPrice;
	private String mobile;
	private String idcard;
	
	public OrderCreateVo(OrderVerify ov){
		this.arrdate = ov.getCheckinDate();
		this.depdate = ov.getCheckoutDate();
		this.hotelId = ov.getHotelId();
		this.rmType = ov.getRoomType();
		this.roomDesc  = ov.getRoomDesc();
		this.email = ov.getEmail();
		this.gstName = ov.getLinkman();
		this.mobile = ov.getMobile();
		this.idcard = ov.getIdcard();
		String tprice = ov.getPriceInfo();
		if(!StringUtils.isBlank(tprice)){
			 String[] prices = tprice.split(",");
			float p = 0;
			 for(String price:prices){
				 if(price!=null){
					 p+=Float.parseFloat(price);
				 }
			 }
			if(prices[0]!=null){
				this.rmPrice = prices[0];
			}
			this.totalPrice = String .valueOf(p);
		}
		
		
	}

	public String getArrdate() {
		return arrdate;
	}

	public void setArrdate(String arrdate) {
		this.arrdate = arrdate;
	}

	public String getDepdate() {
		return depdate;
	}

	public void setDepdate(String depdate) {
		this.depdate = depdate;
	}

	public String getRmPrice() {
		return rmPrice;
	}

	public void setRmPrice(String rmPrice) {
		this.rmPrice = rmPrice;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getRmType() {
		return rmType;
	}

	public void setRmType(String rmType) {
		this.rmType = rmType;
	}

	public String getGstName() {
		return gstName;
	}

	public void setGstName(String gstName) {
		this.gstName = gstName;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
