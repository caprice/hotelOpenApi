package com.huoli.openapi.vo.result;

import java.util.LinkedList;

public class WapSearchHotelsData {
//	@XStreamAlias("hotels")
	private LinkedList<WapRoomSimpleStatus> hotels;

	public LinkedList<WapRoomSimpleStatus> getHotels() {
		return hotels;
	}

	public void setHotels(LinkedList<WapRoomSimpleStatus> hotels) {
		this.hotels = hotels;
	}
}
