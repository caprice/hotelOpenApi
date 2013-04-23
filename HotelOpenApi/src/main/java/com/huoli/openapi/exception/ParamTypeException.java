package com.huoli.openapi.exception;

public class ParamTypeException extends RuntimeException {

	private static final long serialVersionUID = -5163622085204345261L;

	public ParamTypeException() {
	}

	public ParamTypeException(String msg) {
		super(msg);
	}

	public ParamTypeException(String msg, Throwable e) {
		super(msg, e);
	}
}
