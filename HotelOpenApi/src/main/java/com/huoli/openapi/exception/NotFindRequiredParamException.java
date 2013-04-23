package com.huoli.openapi.exception;

public class NotFindRequiredParamException extends RuntimeException {

	private static final long serialVersionUID = -5163622085204345261L;

	public NotFindRequiredParamException() {
	}

	public NotFindRequiredParamException(String msg) {
		super(msg);
	}
}
