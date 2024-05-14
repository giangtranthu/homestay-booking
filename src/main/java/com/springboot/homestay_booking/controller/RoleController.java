package com.springboot.homestay_booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.homestay_booking.exception.RoleAlreadyExistsException;
import com.springboot.homestay_booking.model.Role;
import com.springboot.homestay_booking.model.User;
import com.springboot.homestay_booking.service.RoleService;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@GetMapping("/all-roles")
	public ResponseEntity<List<Role>> getAllRoles() {
		return new ResponseEntity<List<Role>>(roleService.getRoles(), HttpStatus.FOUND);

	}

	@PostMapping("/create-new-role")
	public ResponseEntity<String> createRole(@RequestBody Role theRole) {
		try {
			roleService.createRole(theRole);
			return ResponseEntity.ok("New role created successfully");
		} catch (RoleAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	@DeleteMapping("/delete/{roleId}")
	public void deleteRole(@PathVariable("roleId") Long roleId) {
		roleService.deleteRole(roleId);
	}

	@PostMapping("/remove-all-users-from-role/{roleId}")
	public Role removeAllUsersFromrole(@PathVariable("roleId") Long roleId) {
		return roleService.removeAllUsersFromRole(roleId);
	}

	@PostMapping("/remove-user-from-role")
	public User removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
		return roleService.removeUserFromRole(userId, roleId);
	}

	@PostMapping("/assign-user-to-role")
	public User assignUserToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
		return roleService.assignRoleToUser(userId, roleId);
	}


}
