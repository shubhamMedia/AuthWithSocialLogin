package com.SpringSecurity.Demo.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SpringSecurity.Demo.Entity.Users;
import com.SpringSecurity.Demo.Repository.UserRepo;

@Service
public class UserDetailsServices implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users users = userRepo.getByUsername(username);

		if (users == null) {
			throw new UsernameNotFoundException("user details not found");
		}

		return new User(users.getEmail(), users.getPassword(), new ArrayList<>());

	}

}
