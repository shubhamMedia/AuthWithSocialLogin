package com.SpringSecurity.Demo.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryHandler implements Consumer<OidcUser> {

	private final UserRepository ur = new UserRepository();

	@Override
	public void accept(OidcUser t) {

		// Capture user in local Data Store on first authentication
		if (this.ur.findByName(t.getEmail()) == null) {
			System.out.println("Saving User Date......  ");
			this.ur.save(t);
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
