package com.huoli.openapi.roomStatus;

import java.io.Serializable;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("room")
public class RoomStatus implements Serializable {
	private static final long serialVersionUID = 6172961791640184143L;
	@XStreamAlias("roomId")
	private String typeid;
	@XStreamAlias("roomDesc")
	private String type;
	private int marketPrice;
	private int memberPrice;
	@XStreamOmitField
	private int vouchSet = 0;// 是否需要担保 0 不需要 1需要
	@XStreamOmitField
	private int isRecord = 0;// 是否固化到数据库中的数据 0 不是 1 是
	@XStreamOmitField
	private String date;
	@XStreamOmitField
	private String dateTime;
	private int status = 2; // 0 无房 1有房 2 未知
	@XStreamOmitField
	private Map<String, String> extra;
	private String priceCollect;// 价格汇总


	public String getPriceCollect() {
		return priceCollect;
	}

	public void setPriceCollect(String priceCollect) {
		this.priceCollect = priceCollect;
	}

	public RoomStatus() {

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}

	public int getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(int memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	public int getVouchSet() {
		return vouchSet;
	}

	public void setVouchSet(int vouchSet) {
		this.vouchSet = vouchSet;
	}

	public int getIsRecord() {
		return isRecord;
	}

	public void setIsRecord(int isRecord) {
		this.isRecord = isRecord;
	}
}
