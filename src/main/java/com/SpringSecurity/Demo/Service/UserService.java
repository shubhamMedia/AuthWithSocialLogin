package com.SpringSecurity.Demo.Service;

import java.util.Map;

import com.SpringSecurity.Demo.Entity.Users;
import com.SpringSecurity.Demo.Model.UserDto;
import com.SpringSecurity.Demo.Model.UserLoginDto;

public interface UserService {

	public UserDto registerUser(UserDto dto) throws Exception;

	public Map<String, Users> getAllUsers();

	public String verifyUser(UserLoginDto dto);

	public Users getUserInfo(String name);

}
