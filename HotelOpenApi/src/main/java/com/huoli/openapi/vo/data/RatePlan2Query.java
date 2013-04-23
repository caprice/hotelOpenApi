package com.huoli.openapi.vo.data;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RatePlan2Query {
	private String channelType;
	private String hotelId;
	private String date;
	private String endDate;
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
		
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
