package com.huoli.openapi.vo.data;

public class UserWxToken {
	private Long id;
	private String token;
	private String fromUser;
	private String reqdata;
	private String createtime;
	private int accesscnt;
	private String accesstime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getReqdata() {
		return reqdata;
	}

	public void setReqdata(String reqdata) {
		this.reqdata = reqdata;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getAccesscnt() {
		return accesscnt;
	}

	public void setAccesscnt(int accesscnt) {
		this.accesscnt = accesscnt;
	}

	public String getAccesstime() {
		return accesstime;
	}

	public void setAccesstime(String accesstime) {
		this.accesstime = accesstime;
	}

}
