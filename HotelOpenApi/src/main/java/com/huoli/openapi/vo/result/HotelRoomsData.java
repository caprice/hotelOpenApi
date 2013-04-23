package com.huoli.openapi.vo.result;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class HotelRoomsData {
	@XStreamImplicit
	private List<RoomStatusData> rooms;

	public List<RoomStatusData> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomStatusData> rooms) {
		this.rooms = rooms;
	}

	
	
}
