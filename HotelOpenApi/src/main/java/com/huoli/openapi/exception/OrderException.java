package com.huoli.openapi.exception;

public class OrderException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5291747638907552579L;

	public OrderException() {
		super();
	}

	public OrderException(String msg) {
		super(msg);
	}

	public OrderException(Exception e) {
		super(e);
	}
}
