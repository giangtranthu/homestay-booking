package com.springboot.homestay_booking.service;

import java.util.List;

import com.springboot.homestay_booking.model.Role;
import com.springboot.homestay_booking.model.User;


public interface RoleService {
	List<Role> getRoles();
	Role createRole(Role theRole);
	
	void deleteRole(Long id);
	
	Role findByName(String name);
	
	User removeUserFromRole(Long userId, Long roleId);
	
	User assignRoleToUser(Long userId, Long roleId);
	
	Role removeAllUsersFromRole(Long roleId);
}
