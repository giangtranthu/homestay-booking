package com.springboot.homestay_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.homestay_booking.exception.InternalServerException;
import com.springboot.homestay_booking.exception.ResourceNotFoundException;
import com.springboot.homestay_booking.model.Room;
import com.springboot.homestay_booking.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

@Service
@RequiredArgsConstructor
public class RoomServiceImplement implements RoomService {
	
	private final RoomRepository roomRepository;
		
	@Override
	public Room addNewRoom(MultipartFile file, String roomType, Float roomPrice) throws SQLException, IOException {
		Room room = new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		if (!file.isEmpty()) {
			byte[] photoByte = file.getBytes();
			Blob photoBlob = new SerialBlob(photoByte);
			room.setPhoto(photoBlob);
		
		}
		
		return roomRepository.save(room);
	}

	@Override
	public List<String> getAllRoomTypes() {
		return roomRepository.findDistinctRoomTypes();
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException{
		Optional<Room> room = roomRepository.findById(roomId);
		if (room.isEmpty()) {
			throw new ResourceNotFoundException("Room not found");
		}
		Blob blobPhoto = room.get().getPhoto();
		if (blobPhoto != null) {
			return blobPhoto.getBytes(1, (int) blobPhoto.length());
		}
		return new byte[0];
	}

	@Override
	public void deleteRoom(Long roomId) {
		Optional<Room> room = roomRepository.findById(roomId);
		if (room.isPresent()) {
			roomRepository.deleteById(roomId);
		}
	}

	@Override
	public Room updateRoom(Long roomId, String roomType, Float roomPrice, byte[] photoBytes) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
		if (roomType != null) {
			room.setRoomType(roomType);
		}
		if (roomPrice != null) {
			room.setRoomPrice(roomPrice);
		}
		if (photoBytes != null && photoBytes.length > 0) {
			try {
				room.setPhoto(new SerialBlob(photoBytes));
				
			} catch (SQLException ex) {
				throw new InternalServerException("Error updating room");
			}
		}
		return roomRepository.save(room);
	}

	@Override
	public Optional<Room> getRoomById(Long roomId) {
		// TODO Auto-generated method stub
		return Optional.of(roomRepository.findById(roomId).get());
	}

	@Override
	public List<Room> getAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate, String roomType) {
		return roomRepository.findAvailableRoomsByDatesAndType(checkinDate, checkoutDate, roomType);
	}
}
