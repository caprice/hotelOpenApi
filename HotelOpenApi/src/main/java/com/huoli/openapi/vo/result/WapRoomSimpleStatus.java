package com.huoli.openapi.vo.result;

import com.huoli.openapi.roomStatus.RoomSimpleStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("hotel")
public class WapRoomSimpleStatus {
	private static final long serialVersionUID = -7643636205336860336L;
	private String hotelId;
	private String hotelType;
	@XStreamAlias("minPrice")
	private Integer price;
	private int status;
	private Double distance;
	@XStreamOmitField
	private String dateTime;

	private String tel;
	private String hotelName;
	private String pic;
	private String url;

	public WapRoomSimpleStatus() {

	}

	public WapRoomSimpleStatus(RoomSimpleStatus room) {
		this.hotelId = room.getHotelId();
		this.hotelType = room.getHotelType();
		this.price = room.getPrice();
		this.status = room.getStatus();
		this.distance = room.getDistance();
		this.dateTime = room.getDateTime();
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public double getDistance() {
		return distance;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
