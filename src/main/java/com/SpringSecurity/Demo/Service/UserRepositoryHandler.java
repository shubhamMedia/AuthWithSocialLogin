package com.SpringSecurity.Demo.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.SpringSecurity.Demo.Entity.Users;
import com.SpringSecurity.Demo.Repository.UserRepo;

@Service
public class UserRepositoryHandler implements Consumer<OidcUser> {

	private final UserRepository ur = new UserRepository();

	@Autowired
	private UserRepo repo;

	@Override
	public void accept(OidcUser t) {

		// Capture user in local Data Store on first authentication
		if (this.ur.findByName(t.getEmail()) == null) {
			System.out.println("Saving User Date......  ");
			this.ur.save(t);

			Users user = this.repo.getByUsername(t.getEmail());

			if (user == null) {
				Users users = new Users();
				users.setEmail(t.getEmail());
				users.setName(t.getFullName());

				this.repo.save(users);
			}

		}

	}

	public Map<String, Map<String, String>> getUser() {
		return this.ur.getUsers();
	}

	static class UserRepository {

//		private final Map<String, OidcUser> userCache = new ConcurrentHashMap<>();
//
//		public OidcUser findByName(String name) {
//			return this.userCache.get(name);
//		}
//
//		public void save(OidcUser oidcUser) {
//			this.userCache.put(oidcUser.getEmail(), oidcUser);
//		}

		private final Map<String, Map<String, String>> userCache = new ConcurrentHashMap<>();

		public Map<String, String> findByName(String name) {
			return this.userCache.get(name);
		}

		public void save(OidcUser oidcUser) {
			Map<String, String> userDetails = new LinkedHashMap<>();

			userDetails.put("picture", oidcUser.getPicture());
			userDetails.put("email", oidcUser.getEmail());
			userDetails.put("Dob", oidcUser.getIdToken().getBirthdate());
			userDetails.put("name", oidcUser.getFullName());
			userDetails.put("gender", oidcUser.getIdToken().getGender());
			userDetails.put("country", oidcUser.getAddress().getCountry());
			userDetails.put("Token", oidcUser.getIdToken().getTokenValue());

			this.userCache.put(oidcUser.getEmail(), userDetails);

		}

		public Map<String, Map<String, String>> getUsers() {
			return userCache;
		}

	}

}
