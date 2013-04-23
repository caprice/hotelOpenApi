package com.huoli.openapi.vo.data;

import java.util.Date;

public class UserWxInfo {
	private Long id;
	private String wxuser;
	private String phone;
	private String busitype;
	private String busidata;
	private int status;
	private int userlevel;
	private int reqcnt;
	private String regtime;
	private Date lastusedtime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWxuser() {
		return wxuser;
	}
	public void setWxuser(String wxuser) {
		this.wxuser = wxuser;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBusitype() {
		return busitype;
	}
	public void setBusitype(String busitype) {
		this.busitype = busitype;
	}
	public String getBusidata() {
		return busidata;
	}
	public void setBusidata(String busidata) {
		this.busidata = busidata;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUserlevel() {
		return userlevel;
	}
	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}
	public int getReqcnt() {
		return reqcnt;
	}
	public void setReqcnt(int reqcnt) {
		this.reqcnt = reqcnt;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public Date getLastusedtime() {
		return lastusedtime;
	}
	public void setLastusedtime(Date lastusedtime) {
		this.lastusedtime = lastusedtime;
	}
	

}
