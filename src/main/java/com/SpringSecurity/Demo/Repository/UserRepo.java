package com.SpringSecurity.Demo.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.SpringSecurity.Demo.Entity.Users;

@Repository
public class UserRepo {

	Map<String, Users> userDetails = new LinkedHashMap<>();

	public void save(Users users) {
		userDetails.put(users.getEmail(), users);
	}

	public Users getByUsername(String username) {
		return userDetails.get(username);
	}

	public Map<String, Users> getAllUsers() {
		return userDetails;
	}

}
