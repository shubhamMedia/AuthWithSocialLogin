package com.SpringSecurity.Demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurity.Demo.Entity.Users;
import com.SpringSecurity.Demo.Model.UserDto;
import com.SpringSecurity.Demo.Model.UserLoginDto;
import com.SpringSecurity.Demo.Service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserDto user) {
		try {

			UserDto users = this.service.registerUser(user);

			return ResponseEntity.ok(users);

		} catch (Exception e) {

			return ResponseEntity.ok(e.getMessage());

		}
	}

	@GetMapping("/info")
	public ResponseEntity<?> getUserInformation(@RequestParam(name = "name", required = false) String name) {
		try {
			Users users = this.service.getUserInfo(name);

			return ResponseEntity.ok(users);
		} catch (Exception e) {

			return ResponseEntity.ok(e.getMessage());
		}

	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers() {
		try {
			Map<String, Users> users = this.service.getAllUsers();

			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
		try {

			String token = this.service.verifyUser(loginDto);

			return ResponseEntity.ok(token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(e.getMessage());
		}
	}

}
