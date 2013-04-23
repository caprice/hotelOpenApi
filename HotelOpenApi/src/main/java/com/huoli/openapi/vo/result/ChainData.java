package com.huoli.openapi.vo.result;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ChainData {
	@XStreamAlias("chains")
	private List<ChainInfo> chains;

	public List<ChainInfo> getChains() {
		return chains;
	}

	public void setChains(List<ChainInfo> chains) {
		this.chains = chains;
	}

}
