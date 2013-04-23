package com.huoli.openapi.roomStatus;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SimpleRoomStatus {
	private String typeid;
	private String type;
	private String usable;// 可用房数量
	/**
	 * 是否有房，0表没房,1表有房，2表未知
	 */
	private int status;

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsable() {
		return usable;
	}

	public void setUsable(String usable) {
		this.usable = usable;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
