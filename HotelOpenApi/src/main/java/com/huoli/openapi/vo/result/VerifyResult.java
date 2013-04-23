package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class VerifyResult extends BaseDataResult {
	private VerifyData data;

	public VerifyData getData() {
		return data;
	}

	public void setData(VerifyData data) {
		this.data = data;
	}

}
