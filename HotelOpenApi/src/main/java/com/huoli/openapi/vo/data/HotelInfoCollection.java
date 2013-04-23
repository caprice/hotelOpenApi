package com.huoli.openapi.vo.data;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
@XStreamAlias("hotel")
public class HotelInfoCollection implements Serializable {

	private static final long serialVersionUID = 1816605322026323222L;
@XStreamAlias("hotelId")
	private Integer id;

	private String hotelType;
@XStreamOmitField
	private String hotelId;

	private String name;

	private String tel;

	private String address;

	private Double lat;

	private Double lnt;

	private String city;

	private String logo;
@XStreamOmitField
	private Date createtime;
@XStreamOmitField
	private String geohash;
	private String introduce;
@XStreamOmitField
	private String extraInfo;

	private String hotelPrefix;
@XStreamOmitField
	private Integer hasdayroom;
	private String status;
	// 附加字段
	private String wifi = "0";

	private String parking = "0";

	private String subway = "0";

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public String getHotelPrefix() {
		return hotelPrefix;
	}

	public void setHotelPrefix(String hotelPrefix) {
		this.hotelPrefix = hotelPrefix;
	}

	public Integer getHasdayroom() {
		return hasdayroom;
	}

	public void setHasdayroom(Integer hasdayroom) {
		this.hasdayroom = hasdayroom;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	public String getSubway() {
		return subway;
	}

	public void setSubway(String subway) {
		this.subway = subway;
	}

}