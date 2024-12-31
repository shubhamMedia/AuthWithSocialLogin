package com.SpringSecurity.Demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurity.Demo.Service.UserRepositoryHandler;

@RestController
public class AuthController {

	@Autowired
	private UserRepositoryHandler userRepo;

	@GetMapping("/register")
	public String registerUser(@AuthenticationPrincipal OidcUser user) {
		try {

			this.userRepo.accept(user);

			return "user register Successfully";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/all/users")
	public Map<String, Map<String, String>> getAllUsers() {
		try {

			return this.userRepo.getUser();

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	// x.authorities[0].idToken.fullname = name
	// x.authorities[0].idToken.birthDate = DOB
	// x.authorities[0].idToken.picture = picture
	// x.authorities[0].idToken.gender = gender
	// x.authorities[0].idToken.address.country = country

}
