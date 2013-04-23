package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class CityListResult extends BaseDataResult{
	private CityData data;

	public CityData getData() {
		return data;
	}

	public void setData(CityData data) {
		this.data = data;
	}

}
