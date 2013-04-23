package com.huoli.openapi.vo.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("city")
public class City {
	private int id;
	private String name;
	private String lat;
	private String lnt;
	private String zhkey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLnt() {
		return lnt;
	}

	public void setLnt(String lnt) {
		this.lnt = lnt;
	}

	public String getZhkey() {
		return zhkey;
	}

	public void setZhkey(String zhkey) {
		this.zhkey = zhkey;
	}

}
