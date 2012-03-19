package com.bus3.common.exception;

public enum BusExceptionType {
	NONE_CONNECTION("没有网络"), //

	;

	private String message;

	private BusExceptionType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
