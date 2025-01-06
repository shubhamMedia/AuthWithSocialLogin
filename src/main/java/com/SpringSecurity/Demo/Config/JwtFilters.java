package com.SpringSecurity.Demo.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.SpringSecurity.Demo.Repository.UserRepo;
import com.SpringSecurity.Demo.Service.UserDetailsServices;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilters extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServices detailsServices;

	@Autowired
	private UserRepo uesrRepo;

	@Autowired
	private JwtToken jwtToken;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String username = null;
		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);

			username = this.jwtToken.getUsernameFromtoken(token);

		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails details = this.detailsServices.loadUserByUsername(username);

			if (this.jwtToken.validateToken(token, details)) {

				UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(details, null,
						details.getAuthorities());

				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(upat);
				;

			}
		}

		filterChain.doFilter(request, response);

	}

}
