package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class HotelCollectionResult extends BaseDataResult{
	@XStreamAlias("data")
	private HotelData hotelData;

	public HotelData getHotelData() {
		return hotelData;
	}

	public void setHotelData(HotelData hotelData) {
		this.hotelData = hotelData;
	}

}
