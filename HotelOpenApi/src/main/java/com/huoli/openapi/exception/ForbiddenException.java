package com.huoli.openapi.exception;

public class ForbiddenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5603363644478754921L;

	public ForbiddenException() {
		super();
	}

	public ForbiddenException(String msg) {
		super(msg);
	}

	public ForbiddenException(Exception e) {
		super(e);
	}
}
