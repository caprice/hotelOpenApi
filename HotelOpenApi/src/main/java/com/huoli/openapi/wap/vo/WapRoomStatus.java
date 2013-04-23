package com.huoli.openapi.wap.vo;

import java.util.List;

import com.huoli.openapi.roomStatus.RoomStatus;

public class WapRoomStatus {
	private String hotelId;
	private String hotelType;
	private String hotelBrand;
	private String hotelName;
	private String add;
	private String startDate;
	private String endDate;
	private List<RoomStatus> rooms;
	public String getHotelBrand() {
		return hotelBrand;
	}
	public void setHotelBrand(String hotelBrand) {
		this.hotelBrand = hotelBrand;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getStartDate() {
		return startDate;
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
	public void setRooms(List<RoomStatus> rooms) {
		this.rooms = rooms;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List getRooms() {
		return rooms;
	}
	
}
