package com.huoli.openapi.orderService;

public class OrderResult {
	public String resultCode;
	public String description;
	public String orderId;

	public String getResultCode() {
		return resultCode;
	}

	public String getDescription() {
		return description;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
