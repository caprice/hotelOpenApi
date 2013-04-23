package com.huoli.openapi.vo.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.huoli.openapi.orderService.OrderVo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("order")
public class OrderDetail {
	private String hotelId;
	private String hotelType;
	private String checkinDate;
	private String checkoutDate;
	private String linkman;
	private String mobile;
	private String email;
	private String roomType;
	private String roomDesc;
	private int roomCount;
	private int status;
	private String oid;
	private String kid;
	private Double totalPrice;

	public OrderDetail() {

	}

	public OrderDetail(OrderVo vo) {
		this.checkinDate = vo.getCheckinTime();
		this.checkoutDate = vo.getCheckoutTime();
		this.email = vo.getEmail();
		this.hotelId = vo.getHotelId();
		this.hotelType = vo.getHotelType();
		this.linkman = vo.getName();
		this.mobile = vo.getTel();
		this.roomCount = vo.getRoomCount();
		this.roomDesc = vo.getRoomDesc();
		this.roomType = vo.getRoomType();
		this.status = vo.getStatus();
		this.oid = vo.getOutId();
		this.kid = vo.getId();
		this.totalPrice = vo.getTotalPrice();
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

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
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

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
