package com.springboot.homestay_booking.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.springboot.homestay_booking.exception.InvalidBookingRequestException;
import com.springboot.homestay_booking.exception.InvalidBookingRequestException;
import com.springboot.homestay_booking.exception.ResourceNotFoundException;
import com.springboot.homestay_booking.model.BookedRoom;
import com.springboot.homestay_booking.model.Room;
import com.springboot.homestay_booking.response.BookingResponse;
import com.springboot.homestay_booking.response.RoomResponse;
import com.springboot.homestay_booking.service.BookingRoomService;
import com.springboot.homestay_booking.service.RoomService;

@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/booking")
public class BookingRoomController {
	
	private final BookingRoomService bookingRoomService;
	private final RoomService roomService;
	
	@GetMapping("all-bookings")
	public ResponseEntity<List<BookingResponse>> getAllBookings() {
		List<BookedRoom> bookings = bookingRoomService.getAllBookings();
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
			
		}
		return ResponseEntity.ok(bookingResponses);
	}
	
	
	private BookingResponse getBookingResponse(BookedRoom booking) {
		Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
		RoomResponse room = new RoomResponse(theRoom.getId(), theRoom.getRoomType(), theRoom.getRoomPrice());
		return new BookingResponse(booking.getBookingId(), booking.getCheckinDate(), booking.getCheckoutDate(), booking.getGuestName(), booking.getGuestEmail(), booking.getNumGuest(), booking.getBookingConfirmationCode(), room);
	}


	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
		try {
			BookedRoom booking = bookingRoomService.findByBookingConfirmationCode(confirmationCode);
			BookingResponse bookingResponse = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		}
		catch (ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
	
	
	@PostMapping("/room/{roomId}/booking")
	public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
										@RequestBody BookedRoom bookingRequest) {
		try {
			String confirmationCode = bookingRoomService.saveBooking(roomId, bookingRequest);
			return ResponseEntity.ok("Booking room successfully! Booking comfirmation code is: " + confirmationCode);
		}
		catch (InvalidBookingRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/user/{email}/bookings")
	public ResponseEntity<List<BookingResponse>> getBookingByEmail(@PathVariable String email) {
		List<BookedRoom> bookings = bookingRoomService.getBookingByEmail(email);
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
	}
	
	
	@DeleteMapping("booking/{bookingId}/delete")
	public void cancelBooking(@PathVariable Long bookingId) {
		bookingRoomService.cancelBooking(bookingId);
	}
}
