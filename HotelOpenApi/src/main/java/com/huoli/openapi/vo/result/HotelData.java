package com.huoli.openapi.vo.result;

import java.util.List;

import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("data")
public class HotelData {
	@XStreamAlias("hotels")
	private List<HotelInfoCollection> hotels;

	public List<HotelInfoCollection> getHotels() {
		return hotels;
	}

	public void setHotels(List<HotelInfoCollection> hotels) {
		this.hotels = hotels;
	}

}
