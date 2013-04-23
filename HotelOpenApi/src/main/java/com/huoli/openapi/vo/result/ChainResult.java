package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class ChainResult  extends BaseDataResult{
	@XStreamAlias("data")
	private ChainData data;

	public ChainData getData() {
		return data;
	}

	public void setData(ChainData data) {
		this.data = data;
	}

}
