package com.SpringSecurity.Demo.Config;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtToken {

	private final SecretKey key = getKey();

	public String generateToken(UserDetails users) {
		Map<String, Object> claims = new HashMap<>();

//		String key = Base64.getEncoder().encodeToString(getKey().getEncoded());
//		byte[] keyBytes = Base64.getDecoder().decode(key);

		return Jwts.builder().claims().add(claims).subject(users.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000))).and().signWith(key).compact();

	}

	public String getUsernameFromtoken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private boolean isTokenexpire(String token) {
		return getExpirationdateFromToken(token).before(new Date());
	}

	public Date getExpirationdateFromToken(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllFromClaims(token);
		return claimsResolver.apply(claims);

	}

	private Claims extractAllFromClaims(String token) {

		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

	}

	public boolean validateToken(String token, UserDetails details) {
		String username = getUsernameFromtoken(token);

		return (username.equals(details.getUsername()) && !isTokenexpire(token));
	}

	private SecretKey getKey() {

		try {
			KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");

			SecretKey sk = generator.generateKey();

//			return sk;

			String key = Base64.getEncoder().encodeToString(sk.getEncoded());

			byte[] keyBytes = Base64.getDecoder().decode(key);

			return Keys.hmacShaKeyFor(keyBytes);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}

	}

}
