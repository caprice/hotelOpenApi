package com.huoli.openapi.exception;

public class HotelStatusException extends RuntimeException {

    private static final long serialVersionUID = 5382724354327465070L;

    public HotelStatusException() {
    }

    public HotelStatusException(String msg) {
        super(msg);
    }
}
