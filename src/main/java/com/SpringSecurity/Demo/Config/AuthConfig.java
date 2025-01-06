package com.SpringSecurity.Demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.SpringSecurity.Demo.Service.UserDetailsServices;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class AuthConfig {

	public final String PUBLIC_API[] = { "/api/user/register", "/api/user/all", "api/user/login" };

	@Autowired
	private UserDetailsServices userDetailsService;

	@Autowired
	private JwtFilters jwtFilters;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

//		httpSecurity.authorizeHttpRequests(request -> request.anyRequest().authenticated())
//				.oauth2Login(Customizer.withDefaults());

		httpSecurity
				.authorizeHttpRequests(
						request -> request.requestMatchers(PUBLIC_API).permitAll().anyRequest().authenticated())
				.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable()).httpBasic(httpBasic -> httpBasic.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.oauth2Login(Customizer.withDefaults())
				.addFilterBefore(jwtFilters, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService);

		return authenticationProvider;

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();

	}

}
