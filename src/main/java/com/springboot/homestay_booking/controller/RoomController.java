package com.springboot.homestay_booking.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.homestay_booking.response.BookingResponse;
import com.springboot.homestay_booking.response.RoomResponse;
import com.springboot.homestay_booking.service.BookingRoomService;
import com.springboot.homestay_booking.service.RoomService;
import com.springboot.homestay_booking.service.UserService;
import com.springboot.homestay_booking.exception.PhotoRetrievalException;
import com.springboot.homestay_booking.exception.ResourceNotFoundException;
import com.springboot.homestay_booking.exception.PhotoRetrievalException;
import com.springboot.homestay_booking.model.BookedRoom;
import com.springboot.homestay_booking.model.Room;
import com.springboot.homestay_booking.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/room")
public class RoomController {

	private final RoomService roomService;

	private final BookingRoomService bookingService;

	@PostMapping("/add/new-room")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType, @RequestParam("roomPrice") Float roomPrice)
			throws SQLException, IOException {

		Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
		RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());

		return ResponseEntity.ok(response);

	}

	@GetMapping("/room/types")
	public List<String> getRoomType() {
		return roomService.getAllRoomTypes();
	}

	@GetMapping("/all-rooms")
	public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
		List<Room> rooms = roomService.getAllRooms();
		List<RoomResponse> roomResponses = new ArrayList<>();
		for (Room room : rooms) {
			byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
			if (photoBytes != null && photoBytes.length > 0) {
				String base64Photo = Base64.encodeBase64String(photoBytes);
				RoomResponse roomResponse = getRoomResponse(room);
				roomResponse.setPhoto(base64Photo);
				roomResponses.add(roomResponse);

			}
		}
		return ResponseEntity.ok(roomResponses);
	}

	@DeleteMapping("/delete/room/{roomId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/update/{roomId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
			@RequestParam(required = false) String roomType, @RequestParam(required = false) Float roomPrice,
			@RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
		byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes()
				: roomService.getRoomPhotoByRoomId(roomId);
		Blob blobPhoto = photoBytes != null & photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
		Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
		theRoom.setPhoto(blobPhoto);
		RoomResponse roomResponse = getRoomResponse(theRoom);
		return ResponseEntity.ok(roomResponse);
	}

	@GetMapping("/room/{roomId}")
	public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
		Optional<Room> theRoom = roomService.getRoomById(roomId);
		return theRoom.map(room -> {
			RoomResponse roomResponse = getRoomResponse(room);
			return ResponseEntity.ok(Optional.of(roomResponse));
		}).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
	}

	@GetMapping("/available-rooms")
	public ResponseEntity<List<RoomResponse>> getAvailableRooms(
			@RequestParam("checkinDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
			@RequestParam("checkoutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
			@RequestParam("roomType") String roomType) throws SQLException {

		List<Room> availableRooms = roomService.getAvailableRooms(checkinDate, checkoutDate, roomType);
		List<RoomResponse> roomResponses = new ArrayList<>();
		for (Room room : availableRooms) {
			byte[] photoByte = roomService.getRoomPhotoByRoomId(room.getId());
			if (photoByte != null && photoByte.length > 0) {
				String photoBase64 = Base64.encodeBase64String(photoByte);
				RoomResponse roomResponse = getRoomResponse(room);
				roomResponse.setPhoto(photoBase64);
				roomResponses.add(roomResponse);
			}
		}
		if (roomResponses.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(roomResponses);
		}
	}

	private RoomResponse getRoomResponse(Room room) {
		List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());

		List<BookingResponse> bookingInfo = bookings.stream().map(booking -> new BookingResponse(booking.getBookingId(),
				booking.getCheckinDate(), booking.getCheckoutDate(), booking.getBookingConfirmationCode())).toList();

		byte[] photoByte = null;
		Blob blobPhoto = room.getPhoto();

		if (blobPhoto != null) {
			try {
				photoByte = blobPhoto.getBytes(1, (int) blobPhoto.length());
			} catch (SQLException e) {
				throw new PhotoRetrievalException("Error retrieving photo");
			}
		}
		return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoByte,
				bookingInfo);
	}

	private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingService.getAllBookingsByRoomId(roomId);
	}

}
