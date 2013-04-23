package com.huoli.openapi.vo.result;

import java.util.List;

import com.huoli.openapi.roomStatus.RoomSimpleStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class SearchHotelsData {
	@XStreamAlias("hotels")
	private List<RoomSimpleStatus> hotels;

	public List<RoomSimpleStatus> getHotels() {
		return hotels;
	}

	public void setHotels(List<RoomSimpleStatus> hotels) {
		this.hotels = hotels;
	}
	
}
