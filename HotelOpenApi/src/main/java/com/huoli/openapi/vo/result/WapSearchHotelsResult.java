package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class WapSearchHotelsResult extends BaseDataResult {
	@XStreamAlias("data")
	private WapSearchHotelsData data;

	public WapSearchHotelsData getData() {
		return data;
	}

	public void setData(WapSearchHotelsData data) {
		this.data = data;
	}
}
