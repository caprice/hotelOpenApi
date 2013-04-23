package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("result")
public class OrderListResult extends BaseDataResult {
	@XStreamAlias("data")
	private OrderListData data;

	public OrderListData getData() {
		return data;
	}

	public void setData(OrderListData data) {
		this.data = data;
	}
}
