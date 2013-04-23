package com.huoli.openapi.vo.data;

import java.util.Date;

public class OrderVerify {
	private Long id;
	private Long channelId;
	private String hotelId;
	private String hotelType;
	private String checkinDate;
	private String checkoutDate;
	private String linkman;
	private String mobile;
	private String idcard;
	private String email;
	private String roomType;
	private String roomDesc;
	private String priceInfo;
	private int roomCount;
	private String extraInfo;
	private String verify;
	private String verifyStatus;
	private Integer userStatus;
	private String kid;
	private int status;
	private Date createTime;
	private Date updateTime;

	public OrderVerify() {

	}

	public OrderVerify(String sName, String sMobile, String sEmail,
			String idCard, String checkinDate, String checkoutDate,
			String hotelId, String hotelType, String roomId, String roomDesc,
			String pricesInfo, Integer roomCount, String extraInfo,
			Long channelId) {
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.createTime = new Date();
		this.email = sEmail;
		this.extraInfo = extraInfo;
		this.hotelId = hotelId;
		this.hotelType = hotelType;
		this.idcard = idCard;
		this.linkman = sName;
		this.mobile = sMobile;
		this.priceInfo = pricesInfo;
		this.roomCount = roomCount;
		this.roomDesc = roomDesc;
		this.roomType = roomId;
		this.channelId = channelId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public String getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckoutDate() {
		return checkoutDate;
	}

	

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "OrderVerify [id=" + id + ", hotelId=" + hotelId
				+ ", hotelType=" + hotelType + ", checkinDate=" + checkinDate
				+ ", checkoutDate=" + checkoutDate + ", linkman=" + linkman
				+ ", mobile=" + mobile + ", idcard=" + idcard + ", email="
				+ email + ", roomType=" + roomType + ", roomDesc=" + roomDesc
				+ ", priceInfo=" + priceInfo + ", roomCount=" + roomCount
				+ ", extraInfo=" + extraInfo + ", verify=" + verify + ", kid="
				+ kid + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", toString()="
				+ super.toString() + "]";
	}

}
