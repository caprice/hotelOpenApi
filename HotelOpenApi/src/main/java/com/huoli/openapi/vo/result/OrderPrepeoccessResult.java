package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class OrderPrepeoccessResult extends BaseDataResult {
	@XStreamAlias("data")
	private OrderPreproccessData data;

	public OrderPreproccessData getData() {
		return data;
	}

	public void setData(OrderPreproccessData data) {
		this.data = data;
	}
}
