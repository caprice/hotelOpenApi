package com.huoli.openapi.orderService;

import com.huoli.openapi.vo.result.ProInfo;

public class OrderVo {
    private String checkinTime;
    private String checkoutTime;
    private String createTime;
    private Double firstDayPrice;
    private Double totalPrice;
    private String hotelId;
    private String hotelProduce;
    private String hotelType;
    private String id;
    private String rateType;
    private int roomCount;
    private String roomType;
    private String roomDesc;
    private int status;
    private String statusDesc;
    private String tel;
  

	private String name;
    private String hotelName;
    private String payType;
    private Long uid;
    private String outId;
    private String ciName;
    private String ciMobile;
    private String email;
    private ProInfo proInfo;
    public String getHotelName() {
        return hotelName;
    }

    public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPayType() {
        return payType;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public OrderVo() {

    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Double getFirstDayPrice() {
        return firstDayPrice;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelProduce() {
        return hotelProduce;
    }

    public String getHotelType() {
        return hotelType;
    }

    public String getId() {
        return id;
    }

    public String getRateType() {
        return rateType;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public int getStatus() {
        return status;
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }

    public Long getUid() {
        return uid;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setFirstDayPrice(Double firstDayPrice) {
        this.firstDayPrice = firstDayPrice;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void setHotelProduce(String hotelProduce) {
        this.hotelProduce = hotelProduce;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getCiName() {
        return ciName;
    }

    public void setCiName(String ciName) {
        this.ciName = ciName;
    }

    public String getCiMobile() {
        return ciMobile;
    }

    public void setCiMobile(String ciMobile) {
        this.ciMobile = ciMobile;
    }

	public ProInfo getProInfo() {
		return proInfo;
	}

	public void setProInfo(ProInfo proInfo) {
		this.proInfo = proInfo;
	}

}
