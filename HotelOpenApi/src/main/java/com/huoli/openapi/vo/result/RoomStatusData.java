package com.huoli.openapi.vo.result;

import java.util.List;

import com.huoli.openapi.roomStatus.RoomStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("rooms")
public class RoomStatusData {
	private String date;
	private String endDate;
	@XStreamImplicit
	private List<RoomStatus> rooms;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<RoomStatus> getRooms() {
		return rooms;
	}
	public void setRooms(List<RoomStatus> rooms) {
		this.rooms = rooms;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
