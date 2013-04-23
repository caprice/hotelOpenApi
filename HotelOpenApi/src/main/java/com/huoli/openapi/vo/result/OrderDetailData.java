package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class OrderDetailData {
	@XStreamAlias("order")
	private OrderDetail order;

	public OrderDetail getOrder() {
		return order;
	}

	public void setOrder(OrderDetail order) {
		this.order = order;
	}
}
