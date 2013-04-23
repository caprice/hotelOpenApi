package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("result")
public class SearchHotelsResult extends BaseDataResult {
	@XStreamAlias("data")
	private SearchHotelsData data;

	public SearchHotelsData getData() {
		return data;
	}

	public void setData(SearchHotelsData data) {
		this.data = data;
	}
	
}
