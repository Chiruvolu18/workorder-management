package com.uas.auth.controller;

import com.uas.auth.dto.LoginRequest;
import com.uas.auth.repository.UserRepository;
import com.uas.auth.dto.JwtResponse;
import com.uas.auth.config.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	// Constructor injection
	public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			// Authenticate the user credentials
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			// Generate JWT token
			String token = jwtTokenProvider.generateToken(authentication);

			// Return token in response
			return ResponseEntity.ok(new JwtResponse(token));

		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("Invalid username or password");
		}
	}

}
