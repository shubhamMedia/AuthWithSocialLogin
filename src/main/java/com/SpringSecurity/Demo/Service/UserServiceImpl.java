package com.SpringSecurity.Demo.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SpringSecurity.Demo.Config.JwtToken;
import com.SpringSecurity.Demo.Entity.Users;
import com.SpringSecurity.Demo.Model.UserDto;
import com.SpringSecurity.Demo.Model.UserLoginDto;
import com.SpringSecurity.Demo.Repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsServices detailsServices;

	@Autowired
	private JwtToken jwtToken;

	@Override
	public UserDto registerUser(UserDto dto) throws Exception {

		if (dto.getEmail().isEmpty() || dto.getEmail() == null) {
			throw new Exception("user data invalid");
		}

		Users users = this.userRepo.getByUsername(dto.getEmail());

		if (users != null) {
			throw new Exception("user data already register you can login with you credential");
		}

		Users user = new Users();
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setId(1);

		String encode = passwordEncoder.encode(dto.getPassword());

		user.setPassword(encode);

		this.userRepo.save(user);

		return dto;

	}

	@Override
	public Map<String, Users> getAllUsers() {
		return this.userRepo.getAllUsers();
	}

	@Override
	public String verifyUser(UserLoginDto dto) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getUesrName(), dto.getPassword()));

		if (authentication.isAuthenticated()) {

			UserDetails users = detailsServices.loadUserByUsername(dto.getUesrName());

			return this.jwtToken.generateToken(users);
		}

		return "fail";

	}

	@Override
	public Users getUserInfo(String name) {

		return this.userRepo.getByUsername(name);
	}

}
