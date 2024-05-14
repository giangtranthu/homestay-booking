package com.springboot.homestay_booking.service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.homestay_booking.model.Room;

public interface RoomService {
	
	Room addNewRoom(MultipartFile photo, String roomType, Float roomPrice) throws SQLException, IOException;
	
	List<String> getAllRoomTypes();

	List<Room> getAllRooms();

	byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;

	void deleteRoom(Long roomId);

	Room updateRoom(Long roomId, String roomType, Float roomPrice, byte[] photoBytes);

	Optional<Room> getRoomById(Long roomId);

	List<Room> getAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate, String roomType);
}

