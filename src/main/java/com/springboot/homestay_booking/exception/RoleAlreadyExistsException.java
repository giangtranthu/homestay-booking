package com.springboot.homestay_booking.exception;

public class RoleAlreadyExistsException extends RuntimeException {
	public RoleAlreadyExistsException(String message) {
		super(message);
	}
}
