package com.huoli.openapi.vo.result;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("hotel")
public class SimpleHotelVo {
	private String hotelType;
	private String hotelId;
	private String status;
	private String minPrice;

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
