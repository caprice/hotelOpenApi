package com.huoli.openapi.exception;

public class HotelRoomsServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8527286790031914244L;

	public HotelRoomsServiceException() {
	}

	public HotelRoomsServiceException(String msg) {
		super(msg);
	}

	public HotelRoomsServiceException(String msg, Throwable e) {
		super(msg, e);
	}
}
