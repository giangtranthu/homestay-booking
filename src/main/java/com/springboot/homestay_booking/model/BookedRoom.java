package com.springboot.homestay_booking.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	
	@Column(name="checkinDate")
	private LocalDate checkinDate;
	
	@Column(name="checkoutDate")
	private LocalDate checkoutDate;
	
	@Column(name="guestName")
	private String guestName;
	
	@Column(name="guestEmail")
	private String guestEmail;
	
	
	@Column(name="numGuest")
	private int numGuest;
	
	@Column(name="confirmCode")
	private String bookingConfirmationCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roomID")
	private Room room;
	

	public void setRoom(Room room) {
		this.room = room;
	}


	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}

}
