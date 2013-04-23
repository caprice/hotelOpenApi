package com.huoli.openapi.roomStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("room")
public class ShowRoomStatus extends RoomStatus {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8608140649782515085L;
	private String endDate;// 结束日期
	private String priceCollect;// 价格汇总

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPriceCollect() {
		return priceCollect;
	}

	public void setPriceCollect(String priceCollect) {
		this.priceCollect = priceCollect;
	}
}
