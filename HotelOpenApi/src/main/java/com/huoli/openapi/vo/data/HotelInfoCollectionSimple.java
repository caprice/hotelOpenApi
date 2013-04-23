package com.huoli.openapi.vo.data;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class HotelInfoCollectionSimple implements Serializable {

	private static final long serialVersionUID = 1438474924512552832L;
	@XStreamOmitField
	private Integer id;
	private String hotelType;
	private String hotelId;
	private String name;
	private String address;
	private Double lat;
	private Double lnt;
	private Double radius;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLnt() {
		return lnt;
	}
	public void setLnt(Double lnt) {
		this.lnt = lnt;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	

}