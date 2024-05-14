package com.springboot.homestay_booking.service;

import java.util.List;
import java.util.Optional;

import com.springboot.homestay_booking.model.User;

public interface UserService {
	User registerUser(User user);
	List<User> getUsers();
	void deleteUser(String email);
	User getUser(String email);

}
