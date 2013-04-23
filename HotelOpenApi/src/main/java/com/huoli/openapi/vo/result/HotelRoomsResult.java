package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class HotelRoomsResult extends BaseDataResult {
	@XStreamAlias("data")
	private HotelRoomsData data;

	public HotelRoomsData getData() {
		return data;
	}

	public void setData(HotelRoomsData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "HotelRoomsResult [data=" + data + "]"+super.toString();
	}
	

}
