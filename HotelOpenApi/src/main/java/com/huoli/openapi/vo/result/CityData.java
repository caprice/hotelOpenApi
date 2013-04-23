package com.huoli.openapi.vo.result;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CityData {
	@XStreamAlias("citys")
	private List<City> citys;

	public List<City> getCitys() {
		return citys;
	}

	public void setCitys(List<City> citys) {
		this.citys = citys;
	}

}
