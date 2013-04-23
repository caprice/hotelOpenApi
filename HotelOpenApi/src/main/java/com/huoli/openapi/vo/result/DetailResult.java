package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class DetailResult extends BaseDataResult{
	@XStreamAlias("data")
	private OrderDetailData data;

	public OrderDetailData getData() {
		return data;
	}

	public void setData(OrderDetailData data) {
		this.data = data;
	}
	
}
