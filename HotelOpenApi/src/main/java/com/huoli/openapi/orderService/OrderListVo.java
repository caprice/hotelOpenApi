package com.huoli.openapi.orderService;

import java.util.List;

public class OrderListVo {
	private String sinceId;
	private List<OrderVo> orders;

	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
	}

	public String getSinceId() {
		return sinceId;
	}

	public void setOrders(List<OrderVo> orders) {
		this.orders = orders;
	}

	public List<OrderVo> getOrders() {
		return orders;
	}

}
