/**
 * Copyright (C), 2011-2016 huoli.com
 * File Name: ProInfo.java
 * Encoding: UTF-8
 * Date: 2013-1-28
 */
package com.huoli.openapi.vo.result;

import java.util.Date;

/**
 * 单店优惠信息相关实体类
 * @author  Chris.Zhang (luxury.zhangh@gmail.com)
 * @date 2013-1-28
 */
public class ProInfo {

	private String simpleContentDesc;//优惠标题
	
	private String detailIcon;//优惠logo
	
	private String detailContentDesc ;//优惠具体内容
	
	private Date beginTime;
	
	private Date endTime;

	public String getSimpleContentDesc() {
		return simpleContentDesc;
	}

	public void setSimpleContentDesc(String simpleContentDesc) {
		this.simpleContentDesc = simpleContentDesc;
	}

	public String getDetailIcon() {
		return detailIcon;
	}

	public void setDetailIcon(String detailIcon) {
		this.detailIcon = detailIcon;
	}

	public String getDetailContentDesc() {
		return detailContentDesc;
	}

	public void setDetailContentDesc(String detailContentDesc) {
		this.detailContentDesc = detailContentDesc;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
