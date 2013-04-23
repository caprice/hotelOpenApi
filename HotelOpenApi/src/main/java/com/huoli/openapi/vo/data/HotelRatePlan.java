package com.huoli.openapi.vo.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class HotelRatePlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8545248307255660320L;
	private Long id;
	private String hotelId;
	private String channelType;
	private String hotelName;
	private String roomType;
	private String roomTypeDesc;
	private String date;
	private float priceMarket;
	private float privceMember;
	private String rateType;
	private String enableOrderNum;
	private int status;
	private String extraInfo;
	private Date createTime;
	private Date statusUpdateTime;
	private Date priceUpdateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getRoomTypeDesc() {
		return roomTypeDesc;
	}

	public void setRoomTypeDesc(String roomTypeDesc) {
		this.roomTypeDesc = roomTypeDesc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getPriceMarket() {
		return priceMarket;
	}

	public void setPriceMarket(float priceMarket) {
		this.priceMarket = priceMarket;
	}

	public float getPrivceMember() {
		return privceMember;
	}

	public void setPrivceMember(float privceMember) {
		this.privceMember = privceMember;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getEnableOrderNum() {
		return enableOrderNum;
	}

	public void setEnableOrderNum(String enableOrderNum) {
		this.enableOrderNum = enableOrderNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Date getStatusUpdateTime() {
		return statusUpdateTime;
	}

	public void setStatusUpdateTime(Date statusUpdateTime) {
		this.statusUpdateTime = statusUpdateTime;
	}

	public Date getPriceUpdateTime() {
		return priceUpdateTime;
	}

	public void setPriceUpdateTime(Date priceUpdateTime) {
		this.priceUpdateTime = priceUpdateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
