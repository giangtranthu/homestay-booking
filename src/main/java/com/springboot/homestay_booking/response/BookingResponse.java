package com.springboot.homestay_booking.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

	private Long bookingId;

	private LocalDate checkinDate;

	private LocalDate checkoutDate;

	private String guestName;

	private String guestEmail;

	private int numGuest;

	private String bookingConfirmationCode;
	
	private RoomResponse room;
	
	public BookingResponse(Long bookingId, LocalDate checkinDate, LocalDate checkoutDate,
			String bookingConfirmationCode) {
		super();
		this.bookingId = bookingId;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.bookingConfirmationCode = bookingConfirmationCode;
	}
	
	

}
