package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class OrderCreateResult extends BaseDataResult {
	@XStreamAlias("data")
	private OrderCreateData data;

	public OrderCreateData getData() {
		return data;
	}

	public void setData(OrderCreateData data) {
		this.data = data;
	}

}
