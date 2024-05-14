package com.springboot.homestay_booking.service;

import java.util.List;

import com.springboot.homestay_booking.model.BookedRoom;

public interface BookingRoomService {

	List<BookedRoom> getAllBookingsByRoomId(Long roomId);

	void cancelBooking(Long bookingId);

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	List<BookedRoom> getAllBookings();

	List<BookedRoom> getBookingByEmail(String email);

}

