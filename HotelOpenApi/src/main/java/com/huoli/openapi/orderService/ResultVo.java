package com.huoli.openapi.orderService;

import java.util.Map;

import com.huoli.openapi.vo.result.ProInfo;

public class ResultVo {
	public String resultCode;
	public String description;
	public Long orderId;
	public String status;
	public String outOId;
	private ProInfo proInfo;
	private String outDesc;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, Object> data;

	public String toString() {
		return new StringBuffer().append("resultCode=").append(resultCode)
				.append(",decription=").append(description).append(",orderId=")
				.append(orderId).toString();
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getDescription() {
		return description;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getOutOId() {
		return outOId;
	}

	public void setOutOId(String outOId) {
		this.outOId = outOId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public ProInfo getProInfo() {
		return proInfo;
	}

	public void setProInfo(ProInfo proInfo) {
		this.proInfo = proInfo;
	}

	public String getOutDesc() {
		return outDesc;
	}

	public void setOutDesc(String outDesc) {
		this.outDesc = outDesc;
	}
}
