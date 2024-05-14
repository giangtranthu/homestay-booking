package com.springboot.homestay_booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.homestay_booking.exception.InvalidBookingRequestException;
import com.springboot.homestay_booking.exception.ResourceNotFoundException;
import com.springboot.homestay_booking.model.BookedRoom;
import com.springboot.homestay_booking.model.Room;
import com.springboot.homestay_booking.repository.BookedRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingRoomServiceImplement implements BookingRoomService {
	private final BookedRoomRepository bookingRepository;
	private final RoomService roomService;
	
	@Override
	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}
	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
		
	}
	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if (bookingRequest.getCheckoutDate().isBefore(bookingRequest.getCheckinDate())) {
			throw new InvalidBookingRequestException("Invalid check in date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, existBookings);
		if (roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		} else {
			throw new InvalidBookingRequestException("Not available room");
		}
		return bookingRequest.getBookingConfirmationCode();
	}


	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckinDate().equals(existingBooking.getCheckinDate())
                                || bookingRequest.getCheckoutDate().isBefore(existingBooking.getCheckoutDate())
                                || (bookingRequest.getCheckinDate().isAfter(existingBooking.getCheckinDate())
                                && bookingRequest.getCheckinDate().isBefore(existingBooking.getCheckoutDate()))
                                || (bookingRequest.getCheckinDate().isBefore(existingBooking.getCheckinDate())

                                && bookingRequest.getCheckoutDate().equals(existingBooking.getCheckoutDate()))
                                || (bookingRequest.getCheckinDate().isBefore(existingBooking.getCheckinDate())

                                && bookingRequest.getCheckoutDate().isAfter(existingBooking.getCheckoutDate()))

                                || (bookingRequest.getCheckinDate().equals(existingBooking.getCheckoutDate())
                                && bookingRequest.getCheckoutDate().equals(existingBooking.getCheckinDate()))

                                || (bookingRequest.getCheckinDate().equals(existingBooking.getCheckoutDate())
                                && bookingRequest.getCheckoutDate().equals(bookingRequest.getCheckinDate()))
                );
    }
	
	
	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));
	}
	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}
	@Override
	public List<BookedRoom> getBookingByEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}

}
