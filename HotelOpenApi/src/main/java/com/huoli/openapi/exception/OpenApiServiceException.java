package com.huoli.openapi.exception;

public class OpenApiServiceException extends RuntimeException {

	private static final long serialVersionUID = -5113982505753616954L;

	public OpenApiServiceException(String msg, Throwable e) {
		super(msg, e);
	}

	public OpenApiServiceException(String msg) {
		super(msg);
	}

	public OpenApiServiceException() {
		super();
	}
}
