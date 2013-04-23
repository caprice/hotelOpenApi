package com.huoli.openapi.vo.result;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("data")
public class OrderListData {
	@XStreamAlias("orders")
	private List<OrderDetail> orders;

	public List<OrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetail> orders) {
		this.orders = orders;
	}
}
